# ImageRecognition

Serverless application based on Amazon Recognition for cats images recognition.

The app constists of serveral lambda-based services.

## Services

1. Crawler - cralws incoming image from html

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
