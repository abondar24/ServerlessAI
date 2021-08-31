resource "aws_lambda_permission" "uploadPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.upload_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.anl.id}/*/${aws_api_gateway_method.anlPostMethod.http_method}${aws_api_gateway_resource.anl.path}"
}

resource "aws_lambda_permission" "analysePermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.analyse_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.anl.id}/*/${aws_api_gateway_method.anlGetMethod.http_method}${aws_api_gateway_resource.anl.path}"
}
