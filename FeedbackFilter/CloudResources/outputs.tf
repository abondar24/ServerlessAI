output "api_invoke_url" {
  description = "Deployment invoke url"
  value       = aws_api_gateway_stage.feedback.invoke_url
}
