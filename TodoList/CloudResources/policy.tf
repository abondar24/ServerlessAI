resource "aws_iam_role" "lambda_exec_role" {
  name = var.lambda_exec_role

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
      "sts:AssumeRole"
      ],
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}



resource "aws_iam_policy" "lambda_logging" {
  name = "lambda_todo_logging"
  path = "/"
  description = "IAM policy for logging from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_logging.arn
}

resource "aws_s3_bucket_policy" "data_bucket_policy" {
  bucket = aws_s3_bucket.dtb.id

  policy = jsonencode({
    Version= "2012-10-17",
    Id="DATAPOLICY"
    Statement= [{
        Action=  "s3:GetObject",
        Resource = [
          aws_s3_bucket.dtb.arn,
          "${aws_s3_bucket.dtb.arn}/*",
        ],
        Principal= "*",
        Effect= "Allow",
        Sid= "PublicReadGetObject"
      }
    ]
  })
}


resource "aws_s3_bucket_policy" "frontend_bucket_policy" {
  bucket = aws_s3_bucket.frb.id

  policy = jsonencode({
    Version= "2012-10-17",
    Id= "FRONTENDPOLICY"
    Statement= [{
        Action=  "s3:GetObject",
        Resource = [
        aws_s3_bucket.frb.arn,
        "${aws_s3_bucket.frb.arn}/*",
        ],
        Principal= "*",
        Effect= "Allow",
        Sid= "PublicReadGetObject"
      }
    ]
  })
}


resource "aws_iam_policy" "lambda_dynamo" {
  name = "lambda_dynamo"
  path = "/"
  description = "IAM policy for accessing from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "dynamodb:DescribeTable",
        "dynamodb:Query",
        "dynamodb:Scan",
        "dynamodb:GetItem",
        "dynamodb:PutItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem"
      ],
      "Resource": "arn:aws:dynamodb:${var.region}:${var.acc_id}:table/${var.dn_table}",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_dynamo" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_dynamo.arn
}

resource "aws_api_gateway_account" "gateway" {
  cloudwatch_role_arn = aws_iam_role.cloudwatch.arn
}

resource "aws_iam_role" "cloudwatch" {
  name = "api_gateway_cloudwatch_global"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": "apigateway.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "cloudwatch" {
  name = "default"
  role = aws_iam_role.cloudwatch.id

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogGroup",
                "logs:CreateLogStream",
                "logs:DescribeLogGroups",
                "logs:DescribeLogStreams",
                "logs:PutLogEvents",
                "logs:GetLogEvents",
                "logs:FilterLogEvents"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}

resource "aws_iam_role" "cognito" {
  name = "cognito_role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Federated": "cognito-identity.amazonaws.com"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
         "StringEquals": {"cognito-identity.amazonaws.com:aud":"${aws_cognito_identity_pool.identityPool.id}"},
         "ForAnyValue:StringLike":{"cognito-identity.amazonaws.com:amr":"authenticated"}
       }
    }
  ]
}
EOF
}

resource "aws_iam_policy" "cognito" {
  name = "cognito"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                  "cognito-sync:*",
                  "cognito-identity:*",
                  "S3:*",
                  "transcribe:*",
                  "polly:*",
                  "lex:*"
],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": "execute-api:Invoke",
            "Resource": "${aws_api_gateway_rest_api.todolist.arn}:${var.region}:${var.acc_id}:*/*"
        }
    ]
}
EOF
}


resource "aws_iam_policy" "lambda_transcribe" {
  name = "lambda_transcribe"
  path = "/"
  description = "IAM policy for accessing transcribe from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
           "transcribe:*"
      ],
      "Resource": "*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_policy" "lambda_s3_note" {
  name = "lambda_s3_note"
  path = "/"
  description = "IAM policy for accessing s3 for note related-lambdas"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
           "s3:*"
      ],
      "Resource": "arn:aws:s3:::${var.dt_bucket}/*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "cognito_att" {
  role = aws_iam_role.cognito.name
  policy_arn = aws_iam_policy.cognito.arn
}


resource "aws_iam_role_policy_attachment" "lambda_transcribe" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_transcribe.arn
}


resource "aws_iam_role_policy_attachment" "lambda_s3_note" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_s3_note.arn
}

resource "aws_iam_policy" "lambda_polly" {
  name = "lambda_polly"
  path = "/"
  description = "IAM policy for accessing polly from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "polly:*"
      ],
      "Resource": "*",
      "Effect": "Allow"
    }
  ]
}
EOF
}


resource "aws_iam_role_policy_attachment" "lambda_polly" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_polly.arn
}
