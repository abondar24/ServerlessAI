#Todo List

Serverless todo list application with chat bot

The app constists of serveral lambda-based services and frontend web application.

Check frontend documentation [here](Frontend/README.MD) 
## Lambda Services

1. TodoApi - set of lambda functions for basic CRUD operations working with data in dynamo db.
   - POST /todo
   - GET /todo  
   - GET /todo/id
   - PUT /todo/id
   - DELETE /todo/id 


## Build and deploy lambda

```
cd <lambda-folder>

./gradlew clean build

cd ../CloudResources
terraform init (only first time )
terraform apply
```
