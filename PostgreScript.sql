drop table casino.user;
CREATE TABLE casino.user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

drop table casino.bet;
CREATE TABLE casino.bet (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES casino.user (id) ON DELETE CASCADE,
    game_type casino.game_type_enum NOT NULL,
    bet_amount DECIMAL(10, 2) NOT NULL,
    result VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE casino.game_type_enum AS ENUM ('SLOT_MACHINE');
