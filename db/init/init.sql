-- USUARIOS
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'ANALISTA', -- ANALISTA, ADMIN
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- CLIENTES
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(30) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    telefono VARCHAR(20),
    email VARCHAR(150),
    direccion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE, -- soft delete
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE datos_financieros (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT UNIQUE NOT NULL REFERENCES clientes(id),

    atraso          INT NOT NULL,
    vivienda        VARCHAR(30) NOT NULL,  -- one-hot en FastAPI
    edad            INT NOT NULL,
    dias_lab        INT NOT NULL,
    exp_sf          NUMERIC(10,2),         -- nullable
    nivel_ahorro    INT NOT NULL,
    ingreso         NUMERIC(12,2) NOT NULL,
    linea_sf        NUMERIC(12,2),         -- nullable
    deuda_sf        NUMERIC(12,2),         -- nullable
    score           INT NOT NULL,
    zona            VARCHAR(50) NOT NULL,  -- one-hot en FastAPI
    clasif_sbs      INT NOT NULL,
    nivel_educ      VARCHAR(30) NOT NULL,  -- ordinal encoding en FastAPI

    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- EVALUACIONES (historial, nunca se sobrescribe)
CREATE TABLE evaluaciones (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    datos_entrada JSONB NOT NULL,        -- snapshot enviado a FastAPI
    prediccion VARCHAR(20) NOT NULL,     -- MOROSO, NO_MOROSO
    probabilidad NUMERIC(5,4) NOT NULL,
    categoria_riesgo VARCHAR(20) NOT NULL, -- BAJO, MEDIO, ALTO
    version_modelo VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- DECISIONES
CREATE TABLE decisiones (
    id BIGSERIAL PRIMARY KEY,
    evaluacion_id BIGINT UNIQUE NOT NULL REFERENCES evaluaciones(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    decision VARCHAR(20) NOT NULL, -- APROBADO, RECHAZADO, EN_REVISION
    comentario TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
-- TOKENS
CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(512) UNIQUE NOT NULL,
    token_type VARCHAR(20) NOT NULL DEFAULT 'BEARER',
    revocado BOOLEAN NOT NULL DEFAULT FALSE,
    expirado BOOLEAN NOT NULL DEFAULT FALSE,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE
);
CREATE INDEX idx_tokens_usuario_id ON tokens(usuario_id);
CREATE INDEX idx_tokens_token ON tokens(token);

-- USUARIOS
INSERT INTO usuarios (nombre, apellido, email, password_hash, rol, activo)
VALUES
    ('Carlos', 'Ramírez', 'carlos.ramirez@morascore.com', '$2a$10$dummyhashplaceholder1111111111111111111111111111111111', 'ADMIN', TRUE),
    ('Laura', 'Mendoza', 'laura.mendoza@morascore.com', '$2a$10$dummyhashplaceholder2222222222222222222222222222222222', 'ADMIN', TRUE);

-- CLIENTES
INSERT INTO clientes (usuario_id, tipo_documento, numero_documento, nombre, apellido, fecha_nacimiento, telefono, email, direccion, activo)
VALUES
    (1, 'CC', '1234567890', 'Juan',  'Pérez',    '1985-03-15', '3001234567', 'juan.perez@email.com',    'Calle 10 #5-20, Bogotá',          TRUE),
    (1, 'CC', '0987654321', 'María', 'Gómez',    '1990-07-22', '3109876543', 'maria.gomez@email.com',   'Carrera 45 #12-30, Medellín',     TRUE),
    (2, 'CC', '1122334455', 'Pedro', 'Torres',   '1978-11-08', '3205551234', 'pedro.torres@email.com',  'Av. 68 #23-10, Cali',             TRUE),
    (2, 'CE', '9988776655', 'Ana',   'Martínez', '1995-01-30', '3154443322', 'ana.martinez@email.com',  'Calle 5 #8-15, Barranquilla',     TRUE);

-- DATOS FINANCIEROS
INSERT INTO datos_financieros (cliente_id, atraso, vivienda, edad, dias_lab, exp_sf, nivel_ahorro, ingreso, linea_sf, deuda_sf, score, zona, clasif_sbs, nivel_educ)
VALUES
    (1, 0,  'PROPIA',    40, 1825, 5.50,  3, 3500.00, 10000.00, 2000.00, 720, 'Lima',          1, 'UNIVERSITARIA'),
    (2, 2,  'FAMILIAR',  34,  730, 2.00,  2, 1800.00,  5000.00, 3500.00, 580, 'La Libertad',   2, 'SECUNDARIA'),
    (3, 5,  'FAMILIAR',  46, 3650, 10.00, 1, 2200.00,  8000.00, 7000.00, 450, 'Ancash',        3, 'PRIMARIA'),
    (4, 0,  'PROPIA',    30, 1095, 3.25,  4, 4800.00, 15000.00, 1000.00, 800, 'Lima',          1, 'POSTGRADO');

