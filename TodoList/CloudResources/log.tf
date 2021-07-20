resource "aws_cloudwatch_log_group" "create" {
  name = "/aws/lambda/${var.lambda_create}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "update" {
  name = "/aws/lambda/${var.lambda_update}"
  retention_in_days = 14
}


resource "aws_cloudwatch_log_group" "delete" {
  name = "/aws/lambda/${var.lambda_delete}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "read" {
  name = "/aws/lambda/${var.lambda_read}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "list" {
  name = "/aws/lambda/${var.lambda_list}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_group" "api_gateway" {
  name              = "API-Gateway-Execution-Logs_${aws_api_gateway_rest_api.todolist.id}/test"
  retention_in_days = 7
  
}
