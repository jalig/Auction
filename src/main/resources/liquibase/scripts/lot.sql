-- liquibase formatted sql


-- changeset jalig:2
CREATE TABLE lot
(
    id          BIGSERIAL PRIMARY KEY,
    status      VARCHAR(255) NOT NULL ,
    title       VARCHAR(255) NOT NULL ,
    description VARCHAR(255) NOT NULL ,
    start_price INTEGER,
    bid_price   INTEGER
);