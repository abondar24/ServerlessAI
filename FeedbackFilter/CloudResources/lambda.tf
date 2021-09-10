resource "aws_lambda_function" "feedback_api" {
  s3_bucket = var.lambda_bucket
  s3_key = var.lambda_api_zip
  function_name = var.lambda_feedback_api
  role = aws_iam_role.lambda_exec_role.arn
  handler = var.lambda_feedback_api_handler

  source_code_hash = filebase64sha256("${var.lambda_api_zip_dir}/${var.lambda_api_zip}")

  timeout = 200
  memory_size = 2048
  runtime = var.java

  depends_on = [
    aws_s3_bucket_object.feedback_api_upload,
    aws_iam_role_policy_attachment.lambda_kinesis,
    aws_iam_policy.lambda_logging,
    aws_cloudwatch_log_group.feedback_api,
  ]

}

resource "aws_lambda_function" "translate" {
  s3_bucket = var.lambda_bucket
  s3_key = var.lambda_translate_zip
  function_name = var.lambda_translate
  role = aws_iam_role.lambda_exec_role.arn
  handler = var.lambda_translate_handler

  source_code_hash = filebase64sha256("${var.lambda_translate_zip_dir}/${var.lambda_translate_zip}")

  timeout = 200
  memory_size = 2048
  runtime = var.java

  depends_on = [
    aws_s3_bucket_object.translate_upload,
    aws_iam_role_policy_attachment.lambda_kinesis,
    aws_iam_role_policy_attachment.lambda_translate,
    aws_iam_policy.lambda_logging,
    aws_cloudwatch_log_group.translate,
  ]
}

resource "aws_lambda_event_source_mapping" "translate_source" {
  event_source_arn  = aws_kinesis_stream.feedback_stream.arn
  function_name     = aws_lambda_function.translate.arn
  starting_position = "LATEST"
  batch_size = 100
  enabled = true


}

resource "aws_lambda_function" "sentiment" {
  s3_bucket = var.lambda_bucket
  s3_key = var.lambda_sentiment_zip
  function_name = var.lambda_sentiment
  role = aws_iam_role.lambda_exec_role.arn
  handler = var.lambda_sentiment_handler

  source_code_hash = filebase64sha256("${var.lambda_sentiment_zip_dir}/${var.lambda_sentiment_zip}")

  timeout = 200
  memory_size = 2048
  runtime = var.java

  depends_on = [
    aws_s3_bucket_object.sentiment_upload,
    aws_iam_role_policy_attachment.lambda_kinesis,
    aws_iam_role_policy_attachment.lambda_sentiment,
    aws_iam_policy.lambda_logging,
    aws_cloudwatch_log_group.sentiment,
  ]
}

resource "aws_lambda_event_source_mapping" "sentiment_source" {
  event_source_arn  = aws_kinesis_stream.sentiment_stream.arn
  function_name     = aws_lambda_function.sentiment.arn
  starting_position = "LATEST"
  batch_size = 100
  enabled = true


}
