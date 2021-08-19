# Todo List

Serverless todo list application with chat bot and text to speech support

The app constists of serveral lambda-based services and frontend web application.

Check frontend documentation [here](Frontend/README.MD) 
## Lambda Services

1. TodoApi - set of lambda functions for basic CRUD operations working with data in dynamo db.
   - POST /todo
   - PUT /todo
   - GET /todo  
   - GET /todo/id
   - DELETE /todo/id 
   
2. NoteApi - set of lambda function working with trascribe service
  - POST /note
  - GET /note/id


## Build and deploy lambda

```
cd <lambda-folder>

./gradlew clean build

cd ../CloudResources
terraform init (only first time )
terraform apply
```

# Note

- For tests Local DynamoDB is required

```
docker pull amazon/dynamodb-local

docker run -d -p 8000:8000 --name <container-name> amazon/dynamodb-local
```

- Verify via API Gateway console that the API is actually deployed and deploy manually if not.

- User pool requires manual setup. Check Screenshot below

![Cognito](cognito.png)

- Users added via cognito pool

- Ids are taken from cognito console

- CORS must be enabled manually on API gateway
