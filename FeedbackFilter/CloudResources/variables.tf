variable region {
  default = "eu-west-1"
}

variable "acc_id" {
  default = "203212890819"
}

variable "lambda_exec_role" {
  default = "lambda_id_role"
}

variable "classifier_exec_role" {
  default = "classifier_role"
}

variable "lambda_api_zip" {
  default = "FeedbackApi-1.0-SNAPSHOT.zip"
}

variable "lambda_api_zip_dir" {
  default = "../FeedbackApi/build/distributions"
}

variable "lambda_feedback_api" {
  default = "FeedbackApi"
}

variable "lambda_feedback_api_handler" {
  default = "org.abondar.experimental.feedback.api.handler.FeedbackApiHandler"
}

variable "lambda_translate" {
  default = "Translate"
}

variable "lambda_translate_zip" {
  default = "Translate-1.0-SNAPSHOT.zip"
}

variable "lambda_translate_zip_dir" {
  default = "../Translate/build/distributions"
}

variable "lambda_translate_handler" {
  default = "org.abondar.experimental.feedback.translate.handler.TranslateHandler"
}

variable "lambda_sentiment" {
  default = "Sentiment"
}

variable "lambda_sentiment_zip" {
  default = "Sentiment-1.0-SNAPSHOT.zip"
}

variable "lambda_sentiment_zip_dir" {
  default = "../Sentiment/build/distributions"
}

variable "lambda_sentiment_handler" {
  default = "org.abondar.experimental.feedback.sentiment.handler.SentimentHandler"
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

variable "sentiment_stream" {
  default = "feedback-sent"
}

variable "classifier_stream" {
  default = "feedback-cls"
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

variable "lambda_bucket" {
  default = "feedback-lambdas"
}

variable "train_bucket" {
  default = "feedback-train"
}

variable "result_bucket" {
  default = "feedback-result"
}

variable "archive_type" {
  default = "zip"
}

variable "training_data_dir" {
  default = "../TrainingData"
}

variable "training_data" {
  default = "data.csv"
}
