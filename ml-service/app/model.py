# model.py
import joblib
import pandas as pd
from app.cleaner import MorosidadCleaner  # debe estar ANTES del load

MODEL_PATH = "model/pipeline_morosidad.pkl"
VERSION = "1.0.0"

pipeline = joblib.load(MODEL_PATH)

def predecir(datos: dict) -> dict:
    df = pd.DataFrame([datos])
    mora  = int(pipeline.predict(df)[0])
    proba = float(pipeline.predict_proba(df)[0][1])
    return {"mora": mora, "probabilidad": proba, "version_modelo": VERSION}