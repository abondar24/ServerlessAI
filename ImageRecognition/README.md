# ImageRecognition

Serverless application based on Amazon Recognition for cats images recognition.

The app constists of serveral lambda-based services.

## Services

1. Crawler - cralws incoming image from html.
2. Analyzer - calls AWS rekognition and writes output.json with results.
3. Recognition Service - calls aws to start analysis and to get results.

## Build and deploy lambda

```
cd <lambda-folder>

./gradlew clean build

cd ../cloud-resources

terraform apply
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

Access the server via locahost:8024/recogintion

## SQS Message Body to test
```json
{"action": "download",
  "msg": {
    "url":"http://ai-as-a-service.s3-website-eu-west-1.amazonaws.com"
  }
}
```    
