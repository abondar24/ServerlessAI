resource "aws_api_gateway_rest_api" "anl" {
  name = "anl"
  description = "Id Analyser"
}

resource "aws_api_gateway_resource" "anl" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  parent_id = aws_api_gateway_rest_api.anl.root_resource_id
  path_part = "analysis"

}

resource "aws_api_gateway_resource" "anlGet" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  parent_id = aws_api_gateway_resource.anl.id
  path_part = var.id_path
}

resource "aws_api_gateway_method" "anlPostMethod" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  resource_id = aws_api_gateway_resource.anl.id
  authorization = var.auth_type
  http_method = var.post_method
}

resource "aws_api_gateway_method" "anlGetMethod" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  resource_id = aws_api_gateway_resource.anlGet.id
  authorization = var.auth_type
  http_method = var.get_method
}

resource "aws_api_gateway_integration" "anlUpload" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  resource_id = aws_api_gateway_resource.anl.id
  http_method = aws_api_gateway_method.anlPostMethod.http_method
  integration_http_method = var.post_method
  type = var.integration_type
  uri = aws_lambda_function.upload_func.invoke_arn
}

resource "aws_api_gateway_integration" "anlAnalyse" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  resource_id = aws_api_gateway_resource.anlGet.id
  http_method = aws_api_gateway_method.anlGetMethod.http_method
  integration_http_method = var.post_method
  type = var.integration_type
  uri = aws_lambda_function.analyse_func.invoke_arn
}


resource "aws_api_gateway_deployment" "anl" {
  rest_api_id = aws_api_gateway_rest_api.anl.id

  depends_on = [
    aws_api_gateway_integration.anlUpload,
    aws_api_gateway_integration.anlAnalyse]

  lifecycle {
    create_before_destroy = true

  }

  variables = {
    deployed_at = timestamp()
  }
}
resource "aws_api_gateway_stage" "note" {
  deployment_id = aws_api_gateway_deployment.anl.id
  rest_api_id = aws_api_gateway_rest_api.anl.id
  stage_name = var.stage_test
}

resource "aws_api_gateway_method_settings" "anlSettings" {
  rest_api_id = aws_api_gateway_rest_api.anl.id
  stage_name = aws_api_gateway_stage.note.stage_name
  method_path = var.method_path

  settings {
    metrics_enabled = true
    logging_level = var.log_info
  }
}
