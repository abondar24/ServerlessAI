resource "aws_lambda_permission" "createPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowAPIGatewayInvoke"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.create_func.function_name
  source_arn = "${aws_api_gateway_rest_api.todolist.execution_arn}/*/*"
}
resource "aws_lambda_permission" "updatePermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowAPIGatewayInvoke"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.update_func.function_name
  source_arn = "${aws_api_gateway_rest_api.todolist.execution_arn}/*/*"
}

resource "aws_lambda_permission" "readPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowAPIGatewayInvoke"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.read_func.function_name
  source_arn = "${aws_api_gateway_rest_api.todolist.execution_arn}/*/*"
}

resource "aws_lambda_permission" "listPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowAPIGatewayInvoke"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.list_func.function_name
  source_arn = "${aws_api_gateway_rest_api.todolist.execution_arn}/*/*"
}

resource "aws_lambda_permission" "deletePermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowAPIGatewayInvoke"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.delete_func.function_name
  source_arn = "${aws_api_gateway_rest_api.todolist.execution_arn}/*/*"
}
