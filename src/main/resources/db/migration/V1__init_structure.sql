-- Tabla 'users'
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) UNIQUE NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla 'financial_data'
CREATE TABLE financial_data
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL UNIQUE REFERENCES users(id),

    ingresos_liquidos NUMERIC(15,2),
    ingresos_brutos   NUMERIC(15,2),
    tipo_contrato     VARCHAR(100),
    anos_estabilidad  INTEGER,
    tipo_vivienda     VARCHAR(100),

    gastos_fijos      NUMERIC(15,2),
    deudas_totales    NUMERIC(15,2),
    numero_tarjetas   INTEGER,
    historial_atrasos BOOLEAN,
    dicom             BOOLEAN,
    activos           VARCHAR(500),
    region            VARCHAR(100),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla score_history
CREATE TABLE score_history
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id),
    score_value   INTEGER NOT NULL,
    change_reason VARCHAR(500),
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla credit_request
CREATE TABLE credit_request
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL REFERENCES users(id),
    amount_requested NUMERIC(15,2) NOT NULL,
    status           VARCHAR(50) DEFAULT 'PENDING',
    request_date     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolution_date  TIMESTAMP WITH TIME ZONE,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla audit_log
CREATE TABLE audit_log
(
    id BIGSERIAL PRIMARY KEY,
    action_type VARCHAR(100) NOT NULL,
    user_performed_action BIGINT REFERENCES users(id),
    target_table VARCHAR(100) NOT NULL,
    record_id BIGINT,
    log_details TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Triggers
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp_users
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER set_timestamp_financial_data
    BEFORE UPDATE ON financial_data
    FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER set_timestamp_credit_request
    BEFORE UPDATE ON credit_request
    FOR EACH ROW EXECUTE PROCEDURE update_timestamp();
