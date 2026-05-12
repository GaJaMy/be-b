CREATE TABLE creator (
    id VARCHAR(50) NOT NULL,
    login_id VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_creator PRIMARY KEY (id),
    CONSTRAINT uq_creator_login_id UNIQUE (login_id)
);

CREATE TABLE admin (
    id VARCHAR(50) NOT NULL,
    login_id VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id),
    CONSTRAINT uq_admin_login_id UNIQUE (login_id)
);

CREATE TABLE course (
    id VARCHAR(50) NOT NULL,
    creator_id VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_course PRIMARY KEY (id),
    CONSTRAINT fk_course_creator FOREIGN KEY (creator_id) REFERENCES creator (id)
);

CREATE TABLE sale_record (
    id VARCHAR(50) NOT NULL,
    course_id VARCHAR(50) NOT NULL,
    creator_id VARCHAR(50) NOT NULL,
    student_id VARCHAR(50) NOT NULL,
    amount BIGINT NOT NULL,
    paid_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_sale_record PRIMARY KEY (id),
    CONSTRAINT fk_sale_record_course FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT fk_sale_record_creator FOREIGN KEY (creator_id) REFERENCES creator (id),
    CONSTRAINT ck_sale_record_amount_positive CHECK (amount > 0)
);

CREATE TABLE cancellation_record (
    id VARCHAR(50) NOT NULL,
    sale_record_id VARCHAR(50) NOT NULL,
    creator_id VARCHAR(50) NOT NULL,
    refund_amount BIGINT NOT NULL,
    canceled_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_cancellation_record PRIMARY KEY (id),
    CONSTRAINT fk_cancellation_record_sale_record FOREIGN KEY (sale_record_id) REFERENCES sale_record (id),
    CONSTRAINT fk_cancellation_record_creator FOREIGN KEY (creator_id) REFERENCES creator (id),
    CONSTRAINT ck_cancellation_record_refund_amount_positive CHECK (refund_amount > 0)
);

CREATE INDEX idx_course_creator_id ON course (creator_id);
CREATE INDEX idx_sale_record_course_id ON sale_record (course_id);
CREATE INDEX idx_sale_record_creator_id ON sale_record (creator_id);
CREATE INDEX idx_sale_record_paid_at ON sale_record (paid_at);
CREATE INDEX idx_cancellation_record_sale_record_id ON cancellation_record (sale_record_id);
CREATE INDEX idx_cancellation_record_creator_id ON cancellation_record (creator_id);
CREATE INDEX idx_cancellation_record_canceled_at ON cancellation_record (canceled_at);
