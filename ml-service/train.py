"""
train.py — Entrenar pipeline de morosidad y registrarlo en MLflow.

Uso:
  python train.py

Descarga los datos automáticamente desde Kaggle (luishcaldernb/morosidad)
via kagglehub, entrena el pipeline completo y lo registra en el MLflow
tracking server.

Requiere MLflow tracking server corriendo en MLFLOW_TRACKING_URI
(default: http://127.0.0.1:5000).
"""

import os
import logging

import kagglehub
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, f1_score
from sklearn.pipeline import Pipeline
from sklearn.ensemble import RandomForestClassifier
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder

import mlflow
import mlflow.sklearn

from cleaner import MorosidadCleaner

logging.basicConfig(level=logging.INFO, format="%(levelname)s: %(message)s")
logger = logging.getLogger(__name__)

# Constantes
TRACKING_URI = os.getenv("MLFLOW_TRACKING_URI", "http://127.0.0.1:5000")
EXPERIMENT_NAME = "morascore-riesgo-crediticio"
REGISTERED_MODEL_NAME = "morascore-clasificador"

TARGET = "mora"
CAT_COLS = ["vivienda", "zona"]
EDUCACION_MAPPING = {
    "SIN EDUCACION": 0,
    "PRIMARIA": 1,
    "SECUNDARIA": 2,
    "TECNICA": 3,
    "UNIVERSITARIA": 4,
    "POSTGRADO": 5,
}

RF_PARAMS = {
    "n_estimators": 200,
    "max_depth": 15,
    "min_samples_split": 5,
    "min_samples_leaf": 2,
    "random_state": 42,
}


def main():
    # Descargar datos 
    logger.info("Descargando dataset desde Kaggle (luishcaldernb/morosidad) ...")
    data_dir = kagglehub.dataset_download("luishcaldernb/morosidad")
    csv_path = os.path.join(data_dir, "data.csv")
    logger.info("Dataset descargado en: %s", csv_path)

    df = pd.read_csv(csv_path)
    logger.info("Datos cargados: %d filas, %d columnas", *df.shape)

    # Split
    X = df.drop(columns=[TARGET])
    y = df[TARGET]
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y,
    )
    logger.info("Train: %d | Test: %d", len(X_train), len(X_test))

    # Construir pipeline 
    cleaner = MorosidadCleaner(educacion_mapping=EDUCACION_MAPPING)

    preprocessor = ColumnTransformer(
        transformers=[
            ("cat", OneHotEncoder(handle_unknown="ignore", sparse_output=False), CAT_COLS),
        ],
        remainder="passthrough",
    )

    pipeline = Pipeline([
        ("cleaner", cleaner),
        ("preprocessor", preprocessor),
        ("classifier", RandomForestClassifier(**RF_PARAMS)),
    ])

    # Entrenar 
    logger.info("Entrenando pipeline ...")
    pipeline.fit(X_train, y_train)

    y_pred = pipeline.predict(X_test)
    acc = accuracy_score(y_test, y_pred)
    f1 = f1_score(y_test, y_pred, average="weighted")
    logger.info("Accuracy: %.4f", acc)
    logger.info("F1 Score (weighted): %.4f", f1)

    # Loguear en MLflow 
    mlflow.set_tracking_uri(TRACKING_URI)
    mlflow.set_experiment(EXPERIMENT_NAME)

    with mlflow.start_run(run_name="train-from-kaggle") as run:
        mlflow.log_params(RF_PARAMS)
        mlflow.log_param("test_size", 0.2)
        mlflow.log_param("n_samples", len(df))
        mlflow.log_param("dataset", "luishcaldernb/morosidad")

        mlflow.log_metric("accuracy", acc)
        mlflow.log_metric("f1_score", f1)

        mlflow.sklearn.log_model(
            sk_model=pipeline,
            artifact_path="model",
            registered_model_name=REGISTERED_MODEL_NAME,
        )

        logger.info(
            "Pipeline registrado como '%s' (run_id=%s)",
            REGISTERED_MODEL_NAME,
            run.info.run_id,
        )

    # Automáticamente promover a Production
    from mlflow import MlflowClient
    client = MlflowClient(TRACKING_URI)
    
    # MLflow registra la nueva versión. Como acabamos de crear el experimento/modelo,
    # buscaremos la última versión registrada para promoverla.
    model_name = REGISTERED_MODEL_NAME
    latest_versions = client.search_model_versions(f"name='{model_name}'")
    if latest_versions:
        # Sort by version number descending
        latest_version = sorted(latest_versions, key=lambda v: int(v.version))[-1]
        client.transition_model_version_stage(
            name=model_name,
            version=latest_version.version,
            stage="Production",
            archive_existing_versions=True
        )
        logger.info(f"Modelo {model_name} v{latest_version.version} promovido a Production automáticamente.")


if __name__ == "__main__":
    main()
