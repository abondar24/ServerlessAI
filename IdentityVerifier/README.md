# Identity Verifier

Service for identity verification of id documents

## Modules

The app consists of two modules

1. Analysis API - Lambdas receiving images and starting textract analisys
```
POST api-url/anl/
  Body: image file
  Response:   200 - Image Id 

GET api-url/anl/{imageId} - get analysis response 

Response:   200 - JSON file with analysis results.
{ 
 "nationality":"USA",
 "dateOfBirth":"14-06-1992",
 "placeOfBirth":"New York",
 "expirationDate":"28-10-2021",
 "issueDate":"28-10-2011",
 "givenNames":"Alexandrer",
 "surname":"Bondar","documentNumber":"AA012345"
 }
```

### Build and deploy 

```
cd AnalysysApi

../gradlew clean build

cd ../CloudResources
terraform init (only first time )
terraform apply
```

### Destroy cloud resources
```

cd ../cloud-resources

terraform destroy
```

### Note
- CORS must be enabled manually on API gateway
- Verify via API Gateway console that the API is actually deployed and deploy manually if not.

2. Client - Tiny web client for calling lambdas

### Build and run
```
../gradlew clean build

java -jar <path-to-jar> <path-to-image>
```
