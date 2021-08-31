variable region {
  default = "eu-west-1"
}

variable "acc_id" {
  default = "203212890819"
}

variable "lambda_exec_role" {
  default = "lambda_id_role"
}

variable "bucket_name" {
  default = "anl-bucket"
}

variable "lambda_zip" {
  default = "../AnalysisApi/build/distributions/AnalysisApi-1.0-SNAPSHOT.zip"
}

variable "lambda_upload" {
  default = "IdUpload"
}

variable "lambda_analyse" {
  default = "IdAnalyse"
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

variable "get_method" {
  default = "GET"
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

variable "auth_type" {
  default = "NONE"
}

variable "method_path" {
  default = "*/*"
}
