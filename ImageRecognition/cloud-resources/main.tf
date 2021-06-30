provider "aws" {
  region = var.region
}

resource "aws_s3_bucket" "b" {
  bucket = "img-rec"
  acl = "private"

  tags = {
    Name = "Image rec bucket"
    Environment = "dev"
  }

  lifecycle {

  }

}

resource "aws_sqs_queue" "img_rec_queue" {
  name = "ImgRecQueue"
}

resource "aws_sqs_queue" "img_rec_anl_queue" {
  name = "ImgRecAnlQueue"
}


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
  name = "lambda_logging"
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


resource "aws_iam_policy" "lambda_sqs" {
  name = "lambda_sqs"
  path = "/"
  description = "IAM policy for sqs interaction from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
     "sqs:SendMessage",
      "sqs:ReceiveMessage",
      "sqs:DeleteMessage",
      "sqs:GetQueueAttributes"
      ],
      "Resource": "arn:aws:sqs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_sqs_msg" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_sqs.arn
}


resource "aws_cloudwatch_log_group" "crawler" {
  name = "/aws/lambda/${var.lambda_crawler}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_stream" "crs" {
  name = "CrawlerLogStream"
  log_group_name = aws_cloudwatch_log_group.crawler.name
}



resource "aws_lambda_function" "crawler_func" {
  filename = "../Crawler/build/distributions/${var.lambda_crawler_zip}"
  function_name = var.lambda_crawler
  role = aws_iam_role.lambda_exec_role.arn
  handler = "Handler.requestHandler"

  source_code_hash = filebase64sha256("../Crawler/build/distributions/${var.lambda_crawler_zip}")

  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_cloudwatch_log_group.crawler,
  ]
}

resource "aws_lambda_event_source_mapping" "crawler_func" {
  event_source_arn = aws_sqs_queue.img_rec_queue.arn
  function_name = aws_lambda_function.crawler_func.arn

}

