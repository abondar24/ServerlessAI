resource "aws_lambda_function" "feedback_api" {
  filename = var.lambda_api_zip
  function_name = var.lambda_feedback_api
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.feedback.api.handler.FeedbackApiHandler"

  source_code_hash = filebase64sha256(var.lambda_api_zip)

  timeout = 200
  memory_size = 2048
  runtime = var.java

  depends_on = [
    aws_iam_role_policy_attachment.lambda_kinesis,
    aws_iam_policy.lambda_logging,
    aws_cloudwatch_log_group.feedback_api,
  ]

}
