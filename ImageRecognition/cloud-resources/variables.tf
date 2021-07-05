variable region {
  default = "eu-west-1"
}

variable "lambda_crawler"{
  default = "crawler"
}

variable "lambda_analyzer"{
  default = "analyzer"
}

variable "lambda_endpoints_analyzer"{
  default = "analyze_endpoint"
}

variable "lambda_endpoints_urls"{
  default = "urls_endpoint"
}

variable "lambda_endpoints_images"{
  default = "images_endpoint"
}


variable "lambda_exec_role" {
  default = "lambda_exec_role"
}

variable "lambda_crawler_zip" {
  default = "Crawler-1.0-SNAPSHOT.zip"
}


variable "lambda_analyzer_zip" {
  default = "Analyzer-1.0-SNAPSHOT.zip"
}

variable "lambda_endpoints_analyzer_zip" {
  default = "AnalyzeEndpoint-1.0-SNAPSHOT.zip"
}

variable "lambda_endpoints_images_zip" {
  default = "ImageListEndpoint-1.0-SNAPSHOT.zip"
}

variable "lambda_endpoints_urls_zip" {
  default = "ListUrlEndpoint-1.0-SNAPSHOT.zip"
}

variable "acc_id" {
  default = "203212890819"
}

variable "img_bucket" {
  default = "img-rec"
}

variable "bucket_role" {
  default = "bucket_role"
}
