
resource "aws_cloudwatch_log_group" "crawler" {
  name = "/aws/lambda/${var.lambda_crawler}"
  retention_in_days = 14
}
