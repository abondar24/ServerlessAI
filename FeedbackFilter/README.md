# Feedback Filter

Customer feedback filtering application.


# Modules

## Lambda Services

1. Feedback API -  Entry point lambda with API Gateway posting data to AWS Kinesis stream
```
POST feedback-api-url/
Body: Customer feedback 
    {
      "feedback":"some-feedback",
    }

Response: 
 
 200 - Data posted
 500 - Data posting to Kinesis stream failed
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

# Notes
- CORS must be enabled manually on API gateway
