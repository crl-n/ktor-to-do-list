CREATE SCHEMA IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS app.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(16) NOT NULL UNIQUE,
    password_encoded TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN app.users.password_encoded IS 'Base64 encoded password';

CREATE TABLE IF NOT EXISTS app.tasks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(2048),
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES app.users (id)
);

CREATE TABLE IF NOT EXISTS app.task_collections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES app.users (id)
);

CREATE TABLE IF NOT EXISTS app.tasks_task_collections (
    id SERIAL PRIMARY KEY,
    task_id INTEGER NOT NULL,
    task_collection_id INTEGER NOT NULL,
    FOREIGN KEY (task_id) REFERENCES app.tasks (id),
    FOREIGN KEY (task_collection_id) REFERENCES app.task_collections (id),
    UNIQUE (task_id, task_collection_id)
);
