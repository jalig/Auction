-- liquibase formatted sql


-- changeset jalig:2
CREATE TABLE lot
(
    id         BIGSERIAL PRIMARY KEY,
    status  VARCHAR,
    title VARCHAR,
    description VARCHAR,
    start_price  INTEGER,
    bid_price  INTEGER,
    bid_id BIGINT REFERENCES bid (id)
);