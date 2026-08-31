# cleaner.py
from sklearn.base import BaseEstimator, TransformerMixin
from sklearn.impute import KNNImputer

class MorosidadCleaner(BaseEstimator, TransformerMixin):
    def __init__(self, educacion_mapping):
        self.educacion_mapping = educacion_mapping
        self.imputer = KNNImputer(n_neighbors=5)

    def fit(self, X, y=None):
        self.imputer.fit(X[['exp_sf', 'linea_sf', 'deuda_sf']])
        return self

    def transform(self, X):
        df = X.copy()
        df[['exp_sf', 'linea_sf', 'deuda_sf']] = self.imputer.transform(
            df[['exp_sf', 'linea_sf', 'deuda_sf']]
        )
        df['nivel_educ'] = df['nivel_educ'].map(self.educacion_mapping).fillna(0)
        return df