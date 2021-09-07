resource "aws_api_gateway_rest_api" "feedback" {
  name = "feedback"
  description = "Feedback API"
}

resource "aws_api_gateway_resource" "feedback" {
  rest_api_id = aws_api_gateway_rest_api.feedback.id
  parent_id = aws_api_gateway_rest_api.feedback.root_resource_id
  path_part = "feedback"
}

resource "aws_api_gateway_method" "feedbackPostMethod" {
  rest_api_id = aws_api_gateway_rest_api.feedback.id
  resource_id = aws_api_gateway_resource.feedback.id
  authorization = var.auth_type
  http_method = var.post_method
}

resource "aws_api_gateway_integration" "feedback" {
  rest_api_id = aws_api_gateway_rest_api.feedback.id
  resource_id = aws_api_gateway_resource.feedback.id
  http_method = aws_api_gateway_method.feedbackPostMethod.http_method
  integration_http_method = var.post_method
  type = var.integration_type
  uri = aws_lambda_function.feedback_api.invoke_arn
}


resource "aws_api_gateway_deployment" "feedback" {
  rest_api_id = aws_api_gateway_rest_api.feedback.id
  depends_on = [aws_api_gateway_integration.feedback]

  lifecycle {
    create_before_destroy = true

  }

  variables = {
    deployed_at = timestamp()
  }
}

resource "aws_api_gateway_stage" "feedback" {
  deployment_id = aws_api_gateway_deployment.feedback.id
  rest_api_id = aws_api_gateway_rest_api.feedback.id
  stage_name = var.stage_test
}

resource "aws_api_gateway_method_settings" "feedbackSettings" {
  rest_api_id = aws_api_gateway_rest_api.feedback.id
  stage_name = aws_api_gateway_stage.feedback.stage_name
  method_path = var.method_path

  settings {
    metrics_enabled = true
    logging_level = var.log_info
  }
}
