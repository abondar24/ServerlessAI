variable region {
  default = "eu-west-1"
}

variable "acc_id" {
  default = "203212890819"
}

variable "lambda_exec_role" {
  default = "lambda_todo_role"
}

variable "dt_bucket" {
  default = "td-str"
}

variable "fr_bucket" {
  default = "td-frontend"
}

variable "dn_table" {
  default = "TodoList"
}

variable "lambda_todo_api_zip" {
  default = "../TodoApi/build/distributions/TodoApi-1.0-SNAPSHOT.zip"
}

variable "lambda_note_api_zip" {
  default = "../NoteApi/build/distributions/NoteApi-1.0-SNAPSHOT.zip"
}

variable "lambda_schedule_api_zip" {
  default = "../ScheduleApi/build/distributions/ScheduleApi-1.0-SNAPSHOT.zip"
}

variable "lambda_create" {
  default = "TodoCreate"
}

variable "lambda_update" {
  default = "TodoUpdate"
}

variable "lambda_delete" {
  default = "TodoDelete"
}

variable "lambda_read" {
  default = "TodoRead"
}

variable "lambda_list" {
  default = "TodoList"
}


variable "lambda_transcribe" {
  default = "NoteTranscribe"
}

variable "lambda_note_poll" {
  default = "NotePoll"
}

variable "lambda_schedule_day" {
  default = "ScheduleDay"
}

variable "lambda_schedule_poll" {
  default = "SchedulePoll"
}

variable "origin_id" {
  default = "s3-td-frontend-bucket"
}

variable "auth_type" {
  default = "COGNITO_USER_POOLS"
}

variable "integration_type" {
  default = "AWS_PROXY"
}

variable "java" {
  default = "java11"
}

variable "post_method" {
  default = "POST"
}

variable "put_method" {
  default = "PUT"
}

variable "get_method" {
  default = "GET"
}

variable "delete_method" {
  default = "DELETE"
}

variable "id_path" {
  default = "{id}"
}

variable "log_info" {
  default = "INFO"
}

variable "stage_test" {
  default = "test"
}

variable "method_path" {
  default = "*/*"
}
