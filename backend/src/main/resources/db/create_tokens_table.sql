-- ============================================================
-- Script: crear tabla tokens para la gestión de JWT
-- Ejecutar en la BD PostgreSQL 'morascore' si ddl-auto=validate
-- ============================================================

CREATE TABLE IF NOT EXISTS tokens (
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(512) NOT NULL UNIQUE,
    token_type  VARCHAR(20)  NOT NULL DEFAULT 'BEARER',
    revocado    BOOLEAN      NOT NULL DEFAULT FALSE,
    expirado    BOOLEAN      NOT NULL DEFAULT FALSE,
    usuario_id  BIGINT       NOT NULL,
    CONSTRAINT fk_tokens_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_tokens_usuario_id ON tokens(usuario_id);
CREATE INDEX IF NOT EXISTS idx_tokens_token      ON tokens(token);
