# Ktor Todolist
A humble Todolist api. Kotlin, Ktor, Koin, PostgreSQL, Flyway and more.

## Running the API

### Database setup
Create a Postgres instance using Docker CLI:
```shell
docker run --detach --rm \
  --name postgresql-ktor-to-do-list \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=todolist \
  -p 127.0.0.1:5432:5432 \
  postgres:17.1
```

## Example Requests
You can find example requests for testing the api in [REQUESTS.md](REQUESTS.md).
