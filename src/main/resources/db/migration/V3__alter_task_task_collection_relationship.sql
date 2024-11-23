-- In V1 the relationship between tasks and task collections was defined as
-- a many-to-many relationship. Here we change this to be a many-to-one
-- relationship instead to prevent tasks from being part of several
-- collections at once.

-- Drop junction table
DROP TABLE IF EXISTS app.tasks_task_collections;

-- Replace with foreign key reference in tasks
ALTER TABLE app.tasks ADD COLUMN task_collection_id INTEGER;

ALTER TABLE app.tasks ADD CONSTRAINT fk_task_collection_id
    FOREIGN KEY (task_collection_id) REFERENCES app.task_collections(id);
