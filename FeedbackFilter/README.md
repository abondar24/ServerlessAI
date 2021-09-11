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

2. Translate - Reads posted messages from stream, makes translation and pushes messages to stream for sentiment analysis
3. Sentient -  Detects type of translated feedback. If it is negative,mixed neutral or positive with score less than 85%, it is pushed to classifier stream
4. Classifier - Uses trained classifier and saves json with results to the bucket
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

## Prepare training data
```
cd TrainData

./download.sh
./prepropc.sh
```

## Trainer
Starts training of classifier using uploaded data and creates the endpoint for invoking.

### Build and Run 
```
cd Trainer

../gradlew clean build

java -jar <path-to-jar>/Trainer-1.0-SNAPSHOT-all.jar -c 

java -jar <path-to-jar>/Trainer-1.0-SNAPSHOT-all.jar -e <trained-classifier-arn> 

```

## Client
Uploads data to the app and gets results from bucket

### Build and Run
```
cd Trainer

../gradlew clean build

java -jar <path-to-jar>/Client-1.0-SNAPSHOT-all.jar -u <feedback-json-file>

java -jar <path-to-jar>/Trainer-1.0-SNAPSHOT-all.jar -c <filepath-to-results> 

```
Sample input json
```
 {
  "originalText": "Bad service. I don't like it",
  "source": "twitter",
  "orignator": "@myTwitter"
}
```

# Notes
- CORS must be enabled manually on API gateway
- Before running classifier training all cloud resources must be deployed and data must be uploaded. 
- Only data.csv file is needed for lambda
- Create endpoint via Trainer after classifier is trained
