ALTER TABLE credit_request
    ADD COLUMN IF NOT EXISTS approved_amount NUMERIC(15,2),
    ADD COLUMN IF NOT EXISTS interest_rate_annual NUMERIC(5,2),
    ADD COLUMN IF NOT EXISTS term_months INTEGER,
    ADD COLUMN IF NOT EXISTS monthly_installment NUMERIC(15,2),
    ADD COLUMN IF NOT EXISTS risk_level VARCHAR(50),
    ADD COLUMN IF NOT EXISTS decision_reason VARCHAR(500);
