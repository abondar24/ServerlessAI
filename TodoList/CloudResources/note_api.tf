resource "aws_api_gateway_rest_api" "note" {
  name = "note"
  description = "Note API"
}

resource "aws_api_gateway_resource" "note" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  parent_id = aws_api_gateway_rest_api.note.root_resource_id
  path_part = "note"

}

resource "aws_api_gateway_resource" "notePoll" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  parent_id = aws_api_gateway_resource.note.id
  path_part = var.id_path
}

resource "aws_api_gateway_method" "notePostMethod" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.note.id
  authorization = var.auth_type
  authorizer_id = aws_api_gateway_authorizer.transAuth.id
  http_method = var.post_method
}

resource "aws_api_gateway_method" "noteGetMethod" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.notePoll.id
  authorization = var.auth_type
  authorizer_id = aws_api_gateway_authorizer.pollAuth.id
  http_method = var.get_method
}

resource "aws_api_gateway_integration" "noteTranscribe" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.note.id
  http_method = aws_api_gateway_method.notePostMethod.http_method
  integration_http_method = var.post_method
  type = var.integration_type
  uri = aws_lambda_function.transcribe_func.invoke_arn
}

resource "aws_api_gateway_integration" "notePoll" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.notePoll.id
  http_method = aws_api_gateway_method.noteGetMethod.http_method

  integration_http_method = var.post_method
  type = var.integration_type

  uri = aws_lambda_function.poll_func.invoke_arn
}

resource "aws_api_gateway_deployment" "note" {
  rest_api_id = aws_api_gateway_rest_api.note.id

  depends_on = [
    aws_api_gateway_integration.noteTranscribe,
    aws_api_gateway_integration.notePoll]

  lifecycle {
    create_before_destroy = true

  }

  variables = {
    deployed_at = timestamp()
  }
}
resource "aws_api_gateway_stage" "note" {
  deployment_id = aws_api_gateway_deployment.note.id
  rest_api_id = aws_api_gateway_rest_api.note.id
  stage_name = var.stage_test
}

resource "aws_api_gateway_method_settings" "noteSettings" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  stage_name = aws_api_gateway_stage.note.stage_name
  method_path = var.method_path

  settings {
    metrics_enabled = true
    logging_level = var.log_info
  }
}

resource "aws_api_gateway_authorizer" "transAuth" {
  name = "transAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.note.id
  authorizer_uri = aws_lambda_function.transcribe_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "pollAuth" {
  name = "pollAuth"
  type = var.auth_type
  provider_arns = data.aws_cognito_user_pools.userPools.arns
  rest_api_id = aws_api_gateway_rest_api.note.id
  authorizer_uri = aws_lambda_function.poll_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

