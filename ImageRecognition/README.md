# ImageRecognition

Serverless application based on Amazon Recognition for cats images recognition.

The app constists of serveral lambda-based services.

## Services

1. Crawler - cralws incoming image from html.
2. Analyzer - calls AWS rekognition and writes output.json with results.

## Build

```
cd <service-folder>

./gradlew clean build
```

## Deploy

```
cd ../cloud-resources

terraform apply
```

## SQS Message Body to test
```json
{"action": "download",
  "msg": {
    "url":"http://ai-as-a-service.s3-website-eu-west-1.amazonaws.com"
  }
}
```    
