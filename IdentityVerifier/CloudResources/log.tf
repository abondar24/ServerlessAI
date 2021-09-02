resource "aws_cloudwatch_log_group" "upload" {
  name = "/aws/lambda/${var.lambda_upload}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "analyse" {
  name = "/aws/lambda/${var.lambda_analyse}"
  retention_in_days = 14
}


resource "aws_cloudwatch_log_group" "api_gateway" {
  name              = "API-Gateway-Execution-Logs_${aws_api_gateway_rest_api.anl.id}/test"
  retention_in_days = 14
}
