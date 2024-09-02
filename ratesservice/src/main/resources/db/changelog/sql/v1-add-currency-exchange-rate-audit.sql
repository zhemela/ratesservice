CREATE TYPE action_type AS ENUM ('CREATE', 'UPDATE');

CREATE TABLE currency_exchange_rate_audit (
    audit_id SERIAL PRIMARY KEY,
    currency_name VARCHAR(10) NOT NULL,
    rate DECIMAL(18, 6),
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type action_type
);