variable region {
  default = "eu-west-1"
}

variable "lambda_exec_role_arn" {
  default = "arn:aws:iam::203212890819:role/lambda_exec_role "
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

variable "lambda_api_zip" {
  default = "TodoApi-1.0-SNAPSHOT.zip"
}

variable "lambda_create" {
  default = "TodoCreate"
}

variable "lambda_update" {
  default = "TodoIpdate"
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


