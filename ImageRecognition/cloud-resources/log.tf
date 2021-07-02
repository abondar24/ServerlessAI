
resource "aws_cloudwatch_log_group" "crawler" {
  name = "/aws/lambda/${var.lambda_crawler}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "analyzer" {
  name = "/aws/lambda/${var.lambda_analyzer}"
  retention_in_days = 14
}
