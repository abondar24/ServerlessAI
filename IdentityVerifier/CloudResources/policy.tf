resource "aws_s3_bucket_policy" "id_bucket_policy" {
  bucket = aws_s3_bucket.idb.id

  policy = jsonencode({
    Version= "2012-10-17",
    Id="DATAPOLICY"
    Statement= [{
      Action=  "s3:GetObject",
      Resource = [
        aws_s3_bucket.idb.arn,
        "${aws_s3_bucket.idb.arn}/*",
      ],
      Principal= "*",
      Effect= "Allow",
      Sid= "PublicReadGetObject"
    }
    ]
  })
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
  name = "lambda_ID_logging"
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


resource "aws_iam_policy" "lambda_txt" {
  name = "lambda_text"
  path = "/"
  description = "IAM policy for accessing Textract from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "textract:AnalyzeDocument"
      ],
      "Resource": "*",
      "Effect": "Allow"
    }
  ]
}
EOF
}
resource "aws_iam_role_policy_attachment" "lambda_txt" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_txt.arn
}



resource "aws_iam_policy" "lambda_s3" {
  name = "lambda_s3"
  path = "/"
  description = "IAM policy for accessing s3 from lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
           "s3:*"
      ],
      "Resource": "arn:aws:s3:::${var.bucket_name}/*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_s3" {
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_s3.arn
}
