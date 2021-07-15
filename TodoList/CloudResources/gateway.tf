resource "aws_api_gateway_rest_api" "todolist" {
  name = "todo"
  description = "TodoList API"
}

resource "aws_api_gateway_resource" "todoList" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  parent_id = aws_api_gateway_rest_api.todolist.root_resource_id
  path_part = "todo"

}

resource "aws_api_gateway_resource" "todoReadId" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  parent_id = aws_api_gateway_resource.todoList.id
  path_part = "{id}"
}


resource "aws_api_gateway_method" "postMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  authorization = "NONE"
  http_method = "POST"
}

resource "aws_api_gateway_method" "putMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  authorization = "NONE"
  http_method = "PUT"
}

resource "aws_api_gateway_method" "getMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  authorization = "NONE"
  http_method = "GET"
}

resource "aws_api_gateway_method" "getIdMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoReadId.id
  authorization = "NONE"
  http_method = "GET"
}

resource "aws_api_gateway_method" "deleteMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  authorization = "NONE"
  http_method = "DELETE"
}

resource "aws_api_gateway_integration" "todoCreate" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  http_method = aws_api_gateway_method.postMethod.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.create_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoUpdate" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  http_method = aws_api_gateway_method.putMethod.http_method

  integration_http_method = "PUT"
  type = "AWS_PROXY"
  uri = aws_lambda_function.update_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoRead" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoReadId.id
  http_method = aws_api_gateway_method.getIdMethod.http_method

  integration_http_method = "GET"
  type = "AWS_PROXY"

  uri = aws_lambda_function.read_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoList" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  http_method = aws_api_gateway_method.getMethod.http_method

  integration_http_method = "GET"
  type = "AWS_PROXY"
  uri = aws_lambda_function.list_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoDelete" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  http_method = aws_api_gateway_method.deleteMethod.http_method

  integration_http_method = "DELETE"
  type = "AWS_PROXY"
  uri = aws_lambda_function.delete_func.invoke_arn
}

resource "aws_api_gateway_deployment" "todoList" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id

  lifecycle {
    create_before_destroy = true
  }
}
resource "aws_api_gateway_stage" "todoList" {
  deployment_id = aws_api_gateway_deployment.todoList.id
  rest_api_id   = aws_api_gateway_rest_api.todolist.id
  stage_name    = "test"
}
