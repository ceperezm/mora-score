from fastapi import FastAPI, HTTPException
from app.schemas import DatosEntrada, Prediccion
from app.model import predecir

app = FastAPI(title="MoraScore ML Service")

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/predict", response_model=Prediccion)
def predict(data: DatosEntrada):
    try:
        return predecir(data.model_dump())
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))