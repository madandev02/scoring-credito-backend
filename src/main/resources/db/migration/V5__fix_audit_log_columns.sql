ALTER TABLE audit_log
    ADD COLUMN IF NOT EXISTS access_log_score BOOLEAN;

ALTER TABLE audit_log
    ADD COLUMN IF NOT EXISTS score_before INTEGER;

ALTER TABLE audit_log
    ADD COLUMN IF NOT EXISTS score_after INTEGER;

ALTER TABLE audit_log
    ADD COLUMN IF NOT EXISTS score_reasoning VARCHAR(1000);

ALTER TABLE audit_log
    ADD COLUMN IF NOT EXISTS credit_decision_reason VARCHAR(1000);

ALTER TABLE audit_log
    ADD COLUMN IF NOT EXISTS status VARCHAR(50);
