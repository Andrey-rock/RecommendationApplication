-- liquibase formatted sql
-- changeset andrey-rock:1
CREATE TABLE Dynamic_rule (
    id UUID primary key,
    product_name varchar(20),
    product_id UUID not null,
    product_text varchar(500),
    rule JSON
);

-- changeset andrey-rock:2
CREATE TABLE stats (
    id UUID primary key,
    rule_id UUID not null,
    count INT,
    FOREIGN KEY (rule_id) REFERENCES Dynamic_rule(id) ON DELETE CASCADE
);

-- changeset andrey-rock:3
CREATE EXTENSION IF NOT EXISTS pgcrypto;