variable region {
  default = "eu-west-1"
}

variable "acc_id" {
  default = "203212890819"
}

variable "lambda_exec_role" {
  default = "lambda_id_role"
}

variable "lambda_api_zip" {
  default = "../FeedbackApi/build/distributions/FeedbackApi-1.0-SNAPSHOT.zip"
}

variable "lambda_feedback_api" {
  default = "FeedbackApi"
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

variable "kinesis_stream" {
  default = "feedback-str"
}

variable "method_path" {
  default = "*/*"
}

variable "auth_type" {
  default = "NONE"
}

variable "stage_test" {
  default = "test"
}

variable "log_info" {
  default = "INFO"
}
