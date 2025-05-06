-- liquibase formatted sql
-- changeset andrey-rock:1
CREATE TABLE Dynamic_rule (
    id UUID primary key,
    product_name varchar(20),
    product_id UUID not null,
    product_text varchar(500),
    rule JSON
);