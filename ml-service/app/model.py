# model.py
import os
import sys
import logging

import pandas as pd
import mlflow
import mlflow.sklearn

# Asegurar que cleaner.py importable
# MLflow pueda deserializar el pipeline que contiene MorosidadCleaner.
sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))
from cleaner import MorosidadCleaner  

logger = logging.getLogger(__name__)

# Configuración MLflow
TRACKING_URI = os.getenv("MLFLOW_TRACKING_URI", "http://127.0.0.1:5000")
MODEL_NAME = "morascore-clasificador"
MODEL_STAGE = os.getenv("MLFLOW_MODEL_STAGE", "Production")
MODEL_URI = f"models:/{MODEL_NAME}/{MODEL_STAGE}"

mlflow.set_tracking_uri(TRACKING_URI)

# Carga del modelo
try:
    pipeline = mlflow.sklearn.load_model(MODEL_URI)
    VERSION = f"mlflow-{MODEL_STAGE}"
    logger.info("Modelo cargado desde MLflow: %s", MODEL_URI)
except Exception as e:
    logger.error(
        "No se pudo cargar el modelo desde MLflow (%s): %s", MODEL_URI, e
    )
    raise RuntimeError(
        f"MLflow model registry no disponible o modelo '{MODEL_NAME}' "
        f"no encontrado en stage '{MODEL_STAGE}'. "
        f"Asegúrate de que el tracking server esté corriendo en {TRACKING_URI} "
        f"y que el modelo haya sido promovido a '{MODEL_STAGE}'.\n"
        f"Detalle: {e}"
    ) from e


def predecir(datos: dict) -> dict:
    df = pd.DataFrame([datos])
    mora = int(pipeline.predict(df)[0])
    proba = float(pipeline.predict_proba(df)[0][1])
    return {"mora": mora, "probabilidad": proba, "version_modelo": VERSION}