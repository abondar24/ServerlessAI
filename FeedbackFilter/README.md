# Feedback Filter

Customer feedback filtering application.


# Modules

## Lambda Services

1. Feedback API -  Entry point lambda with API Gateway posting data to AWS Kinesis stream
```
POST feedback-api-url/
Body: Customer feedback 
    {
      "originalText":"some-feedback",
      "source":"twitter",
      "originator":"@someTweet" 
    }

Response: 
 
 200 - Data posted
 500 - Data posting to Kinesis stream failed
 502 - AWS infrastructure not available 
``` 

2. Translate - reads posted messages from stream, makes translation and pushes messages to stream for sentiment analysis
3. Sentient -  detects type of translated feedback. If it is negative,mixed neutral or positive with score less than 85%, it is pushed to classifier stream
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
