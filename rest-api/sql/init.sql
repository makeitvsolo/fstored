CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(60),
    name VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS user_name_idx ON users(name);
