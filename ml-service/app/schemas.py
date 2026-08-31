from pydantic import BaseModel
from typing import Optional
from enum import Enum

class NivelEduc(str, Enum):
    sin_educacion = "SIN EDUCACION"
    primaria      = "PRIMARIA"
    secundaria    = "SECUNDARIA"
    tecnica       = "TECNICA"
    universitaria = "UNIVERSITARIA"
    postgrado     = "POSTGRADO"

class Vivienda(str, Enum):
    familiar  = "FAMILIAR"
    propia    = "PROPIA"
    alquilada = "ALQUILADA"  # si existe en el dataset

class DatosEntrada(BaseModel):
    atraso:       int
    edad:         int
    dias_lab:     int
    exp_sf:       Optional[float] = None  # nullable, KNN lo imputa
    ingreso:      float
    linea_sf:     Optional[float] = None  # nullable, KNN lo imputa
    deuda_sf:     Optional[float] = None  # nullable, KNN lo imputa
    score:        int
    nivel_ahorro: int
    nivel_educ:   NivelEduc
    vivienda:     str   # string libre — OHE maneja categorías desconocidas
    zona:         str   # string libre — igual
    clasif_sbs:   int

class Prediccion(BaseModel):
    mora:          int
    probabilidad:  float
    version_modelo: str