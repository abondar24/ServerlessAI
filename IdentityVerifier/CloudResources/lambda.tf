resource "aws_lambda_function" "upload_func" {
  filename = var.lambda_zip
  function_name = var.lambda_upload
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.identity.analysis.handler.UploadHandler"

  source_code_hash = filebase64sha256(var.lambda_zip)

  timeout = 200
  memory_size = 1024
  runtime = var.java

  depends_on = [
    aws_iam_role_policy_attachment.lambda_s3,
    aws_cloudwatch_log_group.analyse,
  ]

}


resource "aws_lambda_function" "analyse_func" {
  filename = var.lambda_zip
  function_name = var.lambda_analyse
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.identity.analysis.handler.AnalyseHandler"

  source_code_hash = filebase64sha256(var.lambda_zip)

  timeout = 200
  memory_size = 1024
  runtime = var.java

  depends_on = [
    aws_iam_role_policy_attachment.lambda_txt,
    aws_cloudwatch_log_group.analyse,
  ]

}
