# Example Requests

## Httpie
Example requests are provided as Httpie CLI commands.  
Install Httpie CLI: https://httpie.io/cli

## Requests
Add a user
```shell
http :8080/users username=user password=pw
```

Login using form
```shell
http -f :8080/auth/login username=user password=pw
```

Get all users
```shell
http :8080/users Authorization:'Basic '$(echo -n 'user:pw' | base64)
```

Add a task
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) name='Learn Ktor'
```

Add a task with description and priority
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) \
  name='Make a Todolist app' \
  description='Make a Todolist app with Ktor to learn Kotlin backend development' \
  priority='high'
```

Get all tasks
```shell
http :8080/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64)
```

Modify a task's priority
```shell
http patch :8080/tasks/1 Authorization:'Basic '$(echo -n 'user:pw' | base64) priority='high'
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

Add existing task to existing collection
```shell
http :8080/collections/1/tasks Authorization:'Basic '$(echo -n 'user:pw' | base64) \
  taskId=1
```

Delete authenticated user
```shell
http DELETE :8080/users/me Authorization:'Basic '$(echo -n 'user:pw' | base64)
```
