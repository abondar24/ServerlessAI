resource "aws_lambda_function" "crawler_func" {
  filename = "../Crawler/build/distributions/${var.lambda_crawler_zip}"
  function_name = var.lambda_crawler
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.imagerec.crawler.Handler"

  source_code_hash = filebase64sha256("../Crawler/build/distributions/${var.lambda_crawler_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_cloudwatch_log_group.crawler,
  ]
}

resource "aws_lambda_event_source_mapping" "crawler_func" {
  event_source_arn = aws_sqs_queue.img_rec_queue.arn
  function_name = aws_lambda_function.crawler_func.arn

}

resource "aws_lambda_function_event_invoke_config" "invoke_anls" {
  function_name = var.lambda_crawler

  destination_config {
    on_success {
      destination = aws_sqs_queue.img_rec_anl_queue.arn
    }
  }
}
