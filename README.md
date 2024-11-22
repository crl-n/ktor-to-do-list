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

### Test the API with Httpie
Install Httpie CLI: https://httpie.io/cli

Add a user
```shell
http :8080/users username=user password=pw
```

Get all users
```shell
http :8080/users Authorization:'Basic '$(echo -n 'user:pw' | base64)
```

Add a task
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) \
  name='Make a Todolist app' \
  description='Make a Todolist app with Ktor to learn Kotlin backend development'
```

Get all tasks
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64)
```
