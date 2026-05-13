ALTER TABLE sale_record
    ADD COLUMN fee_rate_percent INT NOT NULL DEFAULT 20 AFTER amount;

CREATE TABLE fee_policy (
    id VARCHAR(50) NOT NULL,
    fee_rate_percent INT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_fee_policy PRIMARY KEY (id)
);

CREATE TABLE fee_policy_history (
    id VARCHAR(50) NOT NULL,
    fee_rate_percent INT NOT NULL,
    target_year_month VARCHAR(7) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_fee_policy_history PRIMARY KEY (id),
    CONSTRAINT uk_fee_policy_history_target_year_month UNIQUE (target_year_month)
);

CREATE TABLE settlement (
    id VARCHAR(50) NOT NULL,
    creator_id VARCHAR(50) NOT NULL,
    year_month VARCHAR(7) NOT NULL,
    gross_sales_amount BIGINT NOT NULL,
    refund_amount BIGINT NOT NULL,
    net_sales_amount BIGINT NOT NULL,
    platform_fee_amount BIGINT NOT NULL,
    expected_payout_amount BIGINT NOT NULL,
    sale_count INT NOT NULL,
    cancel_count INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    requested_at DATETIME NOT NULL,
    confirmed_at DATETIME NULL,
    paid_at DATETIME NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_settlement PRIMARY KEY (id),
    CONSTRAINT uk_settlement_creator_year_month UNIQUE (creator_id, year_month)
);

CREATE INDEX idx_settlement_creator_id ON settlement (creator_id);
CREATE INDEX idx_settlement_year_month ON settlement (year_month);
CREATE INDEX idx_fee_policy_history_target_year_month ON fee_policy_history (target_year_month);

INSERT INTO fee_policy (id, fee_rate_percent, created_at, updated_at)
VALUES ('fee-policy-1', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
