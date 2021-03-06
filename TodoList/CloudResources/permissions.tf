resource "aws_lambda_permission" "createPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.create_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.todolist.id}/*/${aws_api_gateway_method.postMethod.http_method}${aws_api_gateway_resource.todoList.path}"
}
resource "aws_lambda_permission" "updatePermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.update_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.todolist.id}/*/${aws_api_gateway_method.putMethod.http_method}${aws_api_gateway_resource.todoList.path}"
}

resource "aws_lambda_permission" "readPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGatewaye"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.read_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.todolist.id}/*/${aws_api_gateway_method.getIdMethod.http_method}${aws_api_gateway_resource.todoReadId.path}"
}

resource "aws_lambda_permission" "listPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.list_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.todolist.id}/*/${aws_api_gateway_method.getMethod.http_method}${aws_api_gateway_resource.todoList.path}"
}

resource "aws_lambda_permission" "deletePermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.delete_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.todolist.id}/*/${aws_api_gateway_method.deleteMethod.http_method}${aws_api_gateway_resource.todoReadId.path}"
}

resource "aws_lambda_permission" "noteTranscribePermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.transcribe_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.note.id}/*/${aws_api_gateway_method.notePostMethod.http_method}${aws_api_gateway_resource.note.path}"
}

resource "aws_lambda_permission" "notePollPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.poll_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.note.id}/*/${aws_api_gateway_method.noteGetMethod.http_method}${aws_api_gateway_resource.notePoll.path}"
}

resource "aws_lambda_permission" "scheduleDayPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.schedule_day_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.schedule.id}/*/${aws_api_gateway_method.schedulePutMethod.http_method}${aws_api_gateway_resource.day.path}"
}

resource "aws_lambda_permission" "schedulePollPermission" {
  action = "lambda:InvokeFunction"
  statement_id = "AllowExecutionFromAPIGateway"
  principal = "apigateway.amazonaws.com"
  function_name = aws_lambda_function.schedule_poll_func.function_name
  source_arn = "arn:aws:execute-api:${var.region}:${var.acc_id}:${aws_api_gateway_rest_api.schedule.id}/*/${aws_api_gateway_method.scheduleGetMethod.http_method}${aws_api_gateway_resource.schedulePoll.path}"
}

