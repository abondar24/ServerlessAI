resource "aws_api_gateway_rest_api" "schedule" {
  name = "schedule"
  description = "Schedule API"
}

resource "aws_api_gateway_resource" "schedule" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  parent_id = aws_api_gateway_rest_api.schedule.root_resource_id
  path_part = "schedule"
}

resource "aws_api_gateway_resource" "day" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  parent_id = aws_api_gateway_resource.schedule.id
  path_part = "day"
}


resource "aws_api_gateway_resource" "schedulePoll" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  parent_id = aws_api_gateway_resource.day.id
  path_part = var.id_path
}

resource "aws_api_gateway_method" "schedulePutMethod" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  resource_id = aws_api_gateway_resource.day.id
  authorization = var.auth_type
  authorizer_id = aws_api_gateway_authorizer.dayAuth.id
  http_method = var.put_method
}

resource "aws_api_gateway_method" "scheduleGetMethod" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  resource_id = aws_api_gateway_resource.schedulePoll.id
  authorization = var.auth_type
  authorizer_id = aws_api_gateway_authorizer.schedulePollAuth.id
  http_method = var.get_method
}

resource "aws_api_gateway_authorizer" "dayAuth" {
  name = "dayAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  authorizer_uri = aws_lambda_function.schedule_day_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "schedulePollAuth" {
  name = "schedulePollAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  authorizer_uri = aws_lambda_function.schedule_poll_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}


resource "aws_api_gateway_integration" "scheduleDay" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  resource_id = aws_api_gateway_resource.day.id
  http_method = aws_api_gateway_method.schedulePutMethod.http_method
  integration_http_method = var.post_method
  type = var.integration_type
  uri = aws_lambda_function.schedule_day_func.invoke_arn
}

resource "aws_api_gateway_integration" "schedulePoll" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  resource_id = aws_api_gateway_resource.schedulePoll.id
  http_method = aws_api_gateway_method.scheduleGetMethod.http_method
  integration_http_method = var.post_method
  type = var.integration_type
  uri = aws_lambda_function.schedule_poll_func.invoke_arn
}


resource "aws_api_gateway_deployment" "schedule" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id

  depends_on = [
    aws_api_gateway_integration.scheduleDay,
    aws_api_gateway_integration.schedulePoll]

  lifecycle {
    create_before_destroy = true

  }

  variables = {
    deployed_at = timestamp()
  }
}

resource "aws_api_gateway_stage" "schedule" {
  deployment_id = aws_api_gateway_deployment.schedule.id
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  stage_name = var.stage_test
}

resource "aws_api_gateway_method_settings" "scheduleSettings" {
  rest_api_id = aws_api_gateway_rest_api.schedule.id
  stage_name = aws_api_gateway_stage.schedule.stage_name
  method_path = var.method_path

  settings {
    metrics_enabled = true
    logging_level = var.log_info
  }
}
