output "api_invoke_url" {
  description = "Deployment invoke url"
  value       = aws_api_gateway_stage.note.invoke_url
}

output "anl_bucket_id" {
  description = "Analysis Bucket Id"
  value       = aws_s3_bucket.idb.id
}
