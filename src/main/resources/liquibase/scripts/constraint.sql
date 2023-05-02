-- liquibase formatted sql

-- changeset jalig:3
ALTER TABLE lot
    ADD CONSTRAINT title_length check (length(title) >= 3 and length(title) <= 64);

-- changeset jalig:4
ALTER TABLE lot
    ADD CONSTRAINT description_length check (length(description) >= 1 and length(description) <= 4096);

-- changeset jalig:5
ALTER TABLE lot
    ADD CONSTRAINT start_price_length check (start_price >= 1 and start_price <= 100);

-- changeset jalig:6
ALTER TABLE lot
    ADD CONSTRAINT bid_price_length check (bid_price >= 1 and bid_price <= 100);