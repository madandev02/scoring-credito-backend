-- =============================
-- USERS
-- =============================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username   VARCHAR(255) UNIQUE NOT NULL,
                       email      VARCHAR(255) UNIQUE NOT NULL,
                       password   VARCHAR(255) NOT NULL,
                       role       VARCHAR(50) NOT NULL,

                       first_name VARCHAR(100),
                       last_name  VARCHAR(100),

                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- FINANCIAL DATA
-- =============================
CREATE TABLE financial_data (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),

                                ingresos_liquidos NUMERIC(15,2),
                                ingresos_brutos   NUMERIC(15,2),
                                tipo_contrato     VARCHAR(100),
                                anos_estabilidad  INTEGER,
                                tipo_vivienda     VARCHAR(100),

                                gastos_fijos NUMERIC(15,2),
                                deudas_totales NUMERIC(15,2),
                                numero_tarjetas INTEGER,
                                historial_atrasos BOOLEAN,
                                dicom BOOLEAN,
                                activos VARCHAR(500),
                                region VARCHAR(100),

                                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- SCORE HISTORY
-- =============================
CREATE TABLE score_history (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL REFERENCES users(id),
                               score_value INTEGER NOT NULL,
                               change_reason VARCHAR(500),
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- CREDIT REQUEST
-- =============================
CREATE TABLE credit_request (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL REFERENCES users(id),
                                amount_requested NUMERIC(15,2) NOT NULL,
                                status VARCHAR(50) DEFAULT 'PENDING',
                                request_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                resolution_date TIMESTAMP WITH TIME ZONE,
                                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- AUDIT LOG (100% compatible)
-- =============================
CREATE TABLE audit_log (
                           id BIGSERIAL PRIMARY KEY,

                           user_id BIGINT REFERENCES users(id),
                           analyst_id BIGINT REFERENCES users(id),

                           action_type VARCHAR(100) NOT NULL,
                           endpoint VARCHAR(255),

                           score_before INTEGER,
                           score_after INTEGER,
                           score_reasoning VARCHAR(1000),

                           credit_decision_reason VARCHAR(1000),

                           access_log_score BOOLEAN,
                           status VARCHAR(50),

                           created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
