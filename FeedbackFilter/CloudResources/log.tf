resource "aws_cloudwatch_log_group" "feedback_api" {
  name = "/aws/lambda/${var.lambda_feedback_api}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "translate" {
  name = "/aws/lambda/${var.lambda_translate}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "sentiment" {
  name = "/aws/lambda/${var.lambda_sentiment}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "api_gateway_feedback" {
  name              = "API-Gateway-Execution-Logs_${aws_api_gateway_rest_api.feedback.id}/test"
  retention_in_days = 14
}
