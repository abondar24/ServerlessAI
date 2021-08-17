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
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.createAuth.id
  http_method = "POST"
}

resource "aws_api_gateway_method" "putMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.updateAuth.id
  http_method = "PUT"
}

resource "aws_api_gateway_method" "getMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.listAuth.id
  http_method = "GET"
}

resource "aws_api_gateway_method" "getIdMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoReadId.id
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.readAuth.id
  http_method = "GET"
}

resource "aws_api_gateway_method" "deleteMethod" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoReadId.id
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.delAuth.id
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

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.update_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoRead" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoReadId.id
  http_method = aws_api_gateway_method.getIdMethod.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"

  uri = aws_lambda_function.read_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoList" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoList.id
  http_method = aws_api_gateway_method.getMethod.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.list_func.invoke_arn
}

resource "aws_api_gateway_integration" "todoDelete" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  resource_id = aws_api_gateway_resource.todoReadId.id
  http_method = aws_api_gateway_method.deleteMethod.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.delete_func.invoke_arn
}

resource "aws_api_gateway_deployment" "todoList" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id

  depends_on = [
    aws_api_gateway_integration.todoCreate,
    aws_api_gateway_integration.todoDelete,
    aws_api_gateway_integration.todoList,
    aws_api_gateway_integration.todoRead,
    aws_api_gateway_integration.todoUpdate]

  lifecycle {
    create_before_destroy = true

  }

  variables = {
    deployed_at = timestamp()
  }
}
resource "aws_api_gateway_stage" "todoList" {
  deployment_id = aws_api_gateway_deployment.todoList.id
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  stage_name = "test"
}

resource "aws_api_gateway_method_settings" "todoSettings" {
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  stage_name = aws_api_gateway_stage.todoList.stage_name
  method_path = "*/*"

  settings {
    metrics_enabled = true
    logging_level = "INFO"
  }
}

resource "aws_api_gateway_authorizer" "createAuth" {
  name = "createAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  authorizer_uri = aws_lambda_function.create_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "updateAuth" {
  name = "updateAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  authorizer_uri = aws_lambda_function.update_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "readAuth" {
  name = "readAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  authorizer_uri = aws_lambda_function.read_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "listAuth" {
  name = "listAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  authorizer_uri = aws_lambda_function.list_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "delAuth" {
  name = "delAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.todolist.id
  authorizer_uri = aws_lambda_function.delete_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}
