# Ktor Todolist
A humble Todolist api. Kotlin, Ktor, PostgreSQL, Flyway and more.

## Database
Create new Postgres database instance:
```shell
docker run --detach --rm \
  --name postgresql-ktor-to-do-list \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=todolist \
  -p 127.0.0.1:5432:5432 \
  postgres:17.1
```
