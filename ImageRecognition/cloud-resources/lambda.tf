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


resource "aws_lambda_function" "analyzer_func" {
  filename = "../Analyzer/build/distributions/${var.lambda_analyzer_zip}"
  function_name = var.lambda_analyzer
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.imagerec.analyzer.Handler"

  source_code_hash = filebase64sha256("../Analyzer/build/distributions/${var.lambda_analyzer_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_cloudwatch_log_group.analyzer,
  ]
}

resource "aws_lambda_event_source_mapping" "analyzer_func" {
  event_source_arn = aws_sqs_queue.img_rec_anl_queue.arn
  function_name = aws_lambda_function.analyzer_func.arn

}


resource "aws_lambda_function" "endpoints_analyzer_func" {
  filename = "../Analyzer/build/distributions/${var.lambda_endpoints_analyzer_zip}"
  function_name = var.lambda_endpoints_analyzer
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.imagerec.endpoints.analyze.Handler"

  source_code_hash = filebase64sha256("../AnalyzeEndpoints/build/distributions/${var.lambda_endpoints_analyzer_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_cloudwatch_log_group.endpoints_analyzer,
  ]
}

resource "aws_lambda_function_event_invoke_config" "invoke_analyzer_func" {
  function_name = var.lambda_endpoints_analyzer

  destination_config {
    on_success {
      destination = aws_sqs_queue.img_rec_queue.arn
    }
  }
}

resource "aws_lambda_function" "endpoints_urls_func" {
  filename = "../Analyzer/build/distributions/${var.lambda_endpoints_urls_zip}"
  function_name = var.lambda_endpoints_urls
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.imagerec.endpoint.listurl.Handler"

  source_code_hash = filebase64sha256("../ListUrlEndpoint/build/distributions/${var.lambda_endpoints_urls_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_cloudwatch_log_group.endpoints_urls,
  ]
}

resource "aws_lambda_function" "endpoints_imgs_func" {
  filename = "../Analyzer/build/distributions/${var.lambda_endpoints_images_zip}"
  function_name = var.lambda_endpoints_images
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.imagerec.endpoint.imagelist.Handler"

  source_code_hash = filebase64sha256("../ImageListEndpoint/build/distributions/${var.lambda_endpoints_images_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_cloudwatch_log_group.endpoints_images,
  ]
}

