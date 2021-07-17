output "deployment_invoke_url" {
  description = "Deployment invoke url"
  value       = aws_api_gateway_stage.todoList.invoke_url
}

output "frontend_bucket_id" {
  description = "Frontend Bucket Id"
  value       = aws_s3_bucket.frb.id
}
