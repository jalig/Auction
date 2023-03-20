-- liquibase formatted sql


-- changeset jalig:2
CREATE TABLE lot
(
    id          BIGSERIAL PRIMARY KEY,
    status      VARCHAR(255),
    title       VARCHAR(255),
    description VARCHAR(255),
    start_price INTEGER,
    bid_price   INTEGER
);