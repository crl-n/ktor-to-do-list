# Example Requests

## Httpie
Example requests are provided as Httpie CLI commands.  
Install Httpie CLI: https://httpie.io/cli

## Requests
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
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) name='Learn Ktor'
```

Add a task with description
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) \
  name='Make a Todolist app' \
  description='Make a Todolist app with Ktor to learn Kotlin backend development'
```

Get all tasks
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64)
```

Add a task collection
```shell
http :8080/collections Authorization:'Basic '$(echo -n 'user:pw' | base64) name='Kotlin backend development'
```

Add a task collection with a description
```shell
http :8080/collections Authorization:'Basic '$(echo -n 'user:pw' | base64) \
  name='Kotlin backend development' \
  description='A collection of tasks aimed at learning Kotlin backend development'
```

Get all task collections
```shell
http :8080/collections Authorization:'Basic '$(echo -n 'user:pw' | base64)
```

Add a new task directly to a task collection
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) \
  name='Make a Todolist app' \
  description='Make a Todolist app with Ktor to learn Kotlin backend development' \
  collectionId=1
```

Delete authenticated user
```shell
http DELETE :8080/users/me Authorization:'Basic '$(echo -n 'user:pw' | base64)
```