resource "aws_lambda_permission" "feedbackPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.feedback_api.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.feedback.id}/*/${aws_api_gateway_method.feedbackPostMethod.http_method}${aws_api_gateway_resource.feedback.path}"
}
