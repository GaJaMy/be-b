ALTER TABLE course
    ADD COLUMN registered_at DATETIME NULL AFTER title;

UPDATE course
SET registered_at = created_at
WHERE registered_at IS NULL;

ALTER TABLE course
    MODIFY COLUMN registered_at DATETIME NOT NULL;

INSERT INTO fee_policy_history (id, fee_rate_percent, effective_started_at, created_at, updated_at)
SELECT 'fee-policy-history-initial', 20, '2000-01-01 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM fee_policy_history
    WHERE id = 'fee-policy-history-initial'
);
