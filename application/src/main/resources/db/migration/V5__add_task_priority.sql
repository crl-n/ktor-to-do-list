CREATE TYPE task_priority AS ENUM('low', 'normal', 'high');

ALTER TABLE app.tasks
    ADD COLUMN priority task_priority NOT NULL DEFAULT 'normal';
