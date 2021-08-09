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
  path_part = "{id}"
}

resource "aws_api_gateway_method" "postMethod" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.note.id
  authorization = aws_api_gateway_authorizer.transAuth
  http_method = "POST"
}

resource "aws_api_gateway_method" "getMethod" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.notePoll.id
  authorization = aws_api_gateway_authorizer.pollAuth
  http_method = "GET"
}

resource "aws_api_gateway_integration" "noteTranscribe" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.note.id
  http_method = aws_api_gateway_method.postMethod.http_method
  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.transcribe_func.invoke_arn
}

resource "aws_api_gateway_integration" "notePoll" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  resource_id = aws_api_gateway_resource.notePoll.id
  http_method = aws_api_gateway_method.getIdMethod.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"

  uri = aws_lambda_function.poll_func.invoke_arn
}

resource "aws_api_gateway_deployment" "note" {
  rest_api_id = aws_api_gateway_rest_api.note.id

  depends_on = [aws_api_gateway_integration.noteTranscribe,
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
  rest_api_id   = aws_api_gateway_rest_api.note.id
  stage_name    = "test"
}

resource "aws_api_gateway_method_settings" "noteSettings" {
  rest_api_id = aws_api_gateway_rest_api.note.id
  stage_name  = aws_api_gateway_stage.note.stage_name
  method_path = "*/*"

  settings {
    metrics_enabled = true
    logging_level   = "INFO"
  }
}

resource "aws_api_gateway_authorizer" "transAuth" {
  name                   = "transAuth"
  rest_api_id            = aws_api_gateway_rest_api.note.id
  authorizer_uri         = aws_lambda_function.transcribe_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

resource "aws_api_gateway_authorizer" "pollAuth" {
  name                   = "pollAuth"
  rest_api_id            = aws_api_gateway_rest_api.note.id
  authorizer_uri         = aws_lambda_function.poll_func.invoke_arn
  authorizer_credentials = aws_iam_role.lambda_exec_role.arn
}

