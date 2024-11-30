CREATE TYPE task_status AS ENUM('pending', 'done');

ALTER TABLE app.tasks
    ADD COLUMN status task_status NOT NULL DEFAULT 'pending';
