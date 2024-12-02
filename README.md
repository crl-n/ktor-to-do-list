# Ktor Todolist
A humble Todolist api. Kotlin, Ktor, Koin, PostgreSQL, Redis, Gradle, Flyway, Test Containers and more.

<img src="https://skillicons.dev/icons?i=kotlin,ktor,postgres,gradle,redis" />

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
The command above is also available as a Gradle task:
```shell
./gradlew runPostgresOnce
```

### Cache setup
Create a Redis instance using Docker CLI:
```shell
docker run --detach --rm \
  --name redis-ktor-to-do-list \
  -p 6379:6379 \
  redis:7.4.1
```

You can examine the cache by connecting to the Redis instance:
```shell
docker exec -it redis-ktor-to-do-list redis-cli
```

### Build
```shell
./gradlew build
```

### Run
Ensure database is up and running, then execute:
```shell
./gradlew run
```

### Test
Note that tests use Test Containers and therefore rely on Docker.  
Ensure that Docker is up and running, then execute:
```shell
./gradlew test
```

## Example Requests
You can find example requests for testing the api in [REQUESTS.md](REQUESTS.md).
