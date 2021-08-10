# Frontend

Frontend web application for TodoList application based on AWS infrastructure.


## Build and run

1. Locally
```
npm install

npm run build

npm run start
```

2. AWS
```
npm install

npm run build

aws s3 sync dist s3://<site-bucket-id>
```

## Access URLs

1. Local - http://localhost:9024
2. AWS - https://s3-eu-west-1.amazonaws.com/td-frontend/index.html

