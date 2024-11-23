-- Add cascading deletes that ensure that all tasks and task collections
-- associated with a user are deleted when that user is deleted.

-- Drop existing constraints that lack cascading delete
ALTER TABLE app.task_collections DROP CONSTRAINT task_collections_user_id_fkey;
ALTER TABLE app.tasks DROP CONSTRAINT tasks_user_id_fkey;

-- Replace dropped constraints with cascading deletes
ALTER TABLE app.task_collections ADD CONSTRAINT task_collections_user_id_fkey
    FOREIGN KEY (user_id)
    REFERENCES app.users(id)
    ON DELETE CASCADE;

ALTER TABLE app.tasks ADD CONSTRAINT tasks_user_id_fkey
    FOREIGN KEY (user_id)
    REFERENCES app.users(id)
    ON DELETE CASCADE;
