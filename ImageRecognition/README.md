# ImageRecognition

Serverless application based on Amazon Recognition for cats images recognition.

The app consists of several lambda-based services.

## Lambda Services

1. Crawler - cralws incoming image from html.
2. Analyzer - calls AWS rekognition and writes output.json with results.
   
## Non-Lambda Service
1. Recognition Service - calls aws to start analysis and to get results.

## Build and deploy cloud resources

```
cd <lambda-folder>

./gradlew clean build

cd ../cloud-resources
terraform init (only first time )

terraform apply
```

## Destroy cloud resources
```

cd ../cloud-resources

terraform destroy
```

## Build and run api

```
cd RecognitionApi

./gradlew clean build 

java -jar <jar-folder>/RecognitionApi-1.0-SNAPSHOT
```

or 

```
cd RecognitionApi

./gradlew clean bootRun

```

## API reference
Access the API via locahost:8024/image/recogintion

```
POST locahost:8024/image/recogintion 

Body:
"msg": {
    "url":"http://ai-as-a-service.s3-website-eu-west-1.amazonaws.com"
  }
  
Response:   200 - Analysis triggered


GET locahost:8024/image/recogintion?domain=ai-as-a-service.s3-website-eu-west-1.amazonaws.com 

Response:   200 - JSON file with analysis results.

```



## SQS Message Body to test via Crawler lambda

```json
{"action": "download",
  "msg": {
    "url":"http://ai-as-a-service.s3-website-eu-west-1.amazonaws.com"
  }
}
```    
