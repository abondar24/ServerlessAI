# Todo List

Serverless todo list application with text to speech and speech to text support

The app constists of serveral lambda-based services and frontend web application.

Check frontend documentation [here](Frontend/README.md) 
## Lambda Services

1. TodoApi - set of lambda functions for basic CRUD operations working with data in dynamo db.
```
POST todo-api-url/todo
Body: Todo item for creating 
    {
      "action":"action",
      "note": "note",
      "checked": false,
      "dueDate": "01/09/2021"
    }

Response: 
 
 200 - Todo Item created
   {
    "id":"217a10f9-c60c-4b29-9599-4ab2e520ea94", 
    "action":"action",
    "note": "note",
    "checked": false,
    "dueDate": "01/09/2021"
   }
 
 404 - DynamoDB table not found
 500 - Malformed request or response body
 502 - AWS infrastructure not available 
 
PUT todo-api-url/todo
Body: Todo item for updating
    {
    "id":"217a10f9-c60c-4b29-9599-4ab2e520ea94", 
    "action":"action",
    "note": "note",
    "checked": false,
    "dueDate": "01/09/2021"
}

Response: 
 
 200 - Todo Item updated
 {
    "id":"217a10f9-c60c-4b29-9599-4ab2e520ea94" ,
    "action":"action",
    "note": "note",
    "checked": false,
    "dueDate": "01/09/2021"
 }
 
 404 - DynamoDB table not found
 500 - Malformed request or response body
 502 - AWS infrastructure not available 

GET todo-api-url/todo/

Response:   
 200 - List of all created items
 [{
    "id":"217a10f9-c60c-4b29-9599-4ab2e520ea94" 
    "action":"action",
    "note": "note",
    "checked": false,
    "dueDate": "01/09/2021"
 }]
 
 404 - DynamoDB table not found
 500 - Malformed request or response body
 502 - AWS infrastructure not available 
 

GET todo-api-url/todo/{imageId}

Response: 
 200 - Single todo item
 
 {
    "id":"217a10f9-c60c-4b29-9599-4ab2e520ea94" 
    "action":"action",
    "note": "note",
    "checked": false,
    "dueDate": "01/09/2021"
 }

 404 - Item not found 
 404 - DynamoDB table not found
 500 - Malformed request or response body
 502 - AWS infrastructure not available 

DELETE todo-api-url/todo/{imageId}

Response: 
 200 - Item deleted
 404 - Item not found 
 404 - DynamoDB table not found
 500 - Malformed request or response body
 502 - AWS infrastructure not available 
```
3. NoteApi - set of lambda functions working with transcribe service
```
POST note-api-url/note -
Body: Note params for starting job
    {
      "noteLang": "en-US",
      "noteUri": "some-uri",
      "noteFormat": "some-format",
      "noteName": "job-name",
      "noteSampleRate": 256
    }

Response: 
  
  200 - Transcribe Job Started
  500 - Malformed request or response body
  502 - AWS infrastructure not available

GET note-api-url/note/{jobId}

Response: 
  200 - Job Result from AWS Transcribe
  404 - Job with id jobId not found
  501 - Job failed
  502 - AWS infrastructure not available
```

3. Schedule API - ser of lambda functions working with polly service
```
PUT schedule-api-url/day 

Response: 
  
  200 - Polly Job Started
  {
    "taskId": "id",
    "taskStatus": "COMPLETED"
    "taskUri": "uri" 
  } 
  
  404 - DynamoDB table not found
  500 - Internal error
  502 - AWS infrastructure not available

GET schedule-api-url/day/{taskId}

Response: 

  200 - Polly Job Result
  {
    "taskId": "id",
    "taskStatus": "COMPLETED"
    "taskUri": "uri" ,
    "signedUrl": "bucket object url"
  } 
  
  500 - Internal error
  502 - AWS infrastructure not available
```

## Build and deploy lambda

```
cd <lambda-folder>

./gradlew clean build

cd ../CloudResources
terraform init (only first time )
terraform apply
```

## Destroy cloud resources
```

cd ../cloud-resources

terraform destroy
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
