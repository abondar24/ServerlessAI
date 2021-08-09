resource "aws_lambda_function" "create_func" {
  filename = "../TodoApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_create
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.todo.api.handler.CreateHandler"

  source_code_hash = filebase64sha256("../TodoApi/build/distributions/${var.lambda_todo_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_dynamo,
    aws_cloudwatch_log_group.create,
  ]

}

resource "aws_lambda_function" "update_func" {
  filename = "../TodoApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_update
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.todo.api.handler.UpdateHandler"

  source_code_hash = filebase64sha256("../TodoApi/build/distributions/${var.lambda_todo_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_dynamo,
    aws_cloudwatch_log_group.update,
  ]

}

resource "aws_lambda_function" "delete_func" {
  filename = "../TodoApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_delete
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.todo.api.handler.DeleteHandler"

  source_code_hash = filebase64sha256("../TodoApi/build/distributions/${var.lambda_todo_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_dynamo,
    aws_cloudwatch_log_group.delete,
  ]

}

resource "aws_lambda_function" "read_func" {
  filename = "../TodoApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_read
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.todo.api.handler.ReadHandler"

  source_code_hash = filebase64sha256("../TodoApi/build/distributions/${var.lambda_todo_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_dynamo,
    aws_cloudwatch_log_group.read,
  ]

}

resource "aws_lambda_function" "list_func" {
  filename = "../TodoApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_list
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.todo.api.handler.ListHandler"

  source_code_hash = filebase64sha256("../TodoApi/build/distributions/${var.lambda_todo_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_dynamo,
    aws_cloudwatch_log_group.list,
  ]

}

resource "aws_lambda_function" "transcribe_func" {
  filename = "../NoteApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_transcribe
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.note.api.handler.TranscribeHandler"

  source_code_hash = filebase64sha256("../NoteApi/build/distributions/${var.lambda_note_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_note,
    aws_cloudwatch_log_group.list,
  ]

}

resource "aws_lambda_function" "transcribe_func" {
  filename = "../NoteApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_transcribe
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.note.api.handler.TranscribeHandler"

  source_code_hash = filebase64sha256("../NoteApi/build/distributions/${var.lambda_note_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_note,
    aws_cloudwatch_log_group.list,
  ]

}


resource "aws_lambda_function" "poll_func" {
  filename = "../NoteApi/build/distributions/${var.lambda_todo_api_zip}"
  function_name = var.lambda_poll
  role = aws_iam_role.lambda_exec_role.arn
  handler = "org.abondar.experimental.note.api.handler.PollHandler"

  source_code_hash = filebase64sha256("../NoteApi/build/distributions/${var.lambda_note_api_zip}")

  timeout = 200
  memory_size = 1024
  runtime = "java11"

  depends_on = [
    aws_iam_role_policy_attachment.lambda_note,
    aws_cloudwatch_log_group.list,
  ]

}
