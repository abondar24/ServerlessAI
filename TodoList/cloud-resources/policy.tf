//uncomment if not created
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
  name = "lambdaDynamo"
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

resource "aws_lambda_permission" "creatPer" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowAPIGatewayInvoke"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.create_func.function_name
  source_arn = "${aws_api_gateway_rest_api.todolist.execution_arn}/*/*"
}

