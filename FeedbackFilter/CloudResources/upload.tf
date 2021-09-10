
resource "aws_s3_bucket_object" "feedback_api_upload" {
  bucket = aws_s3_bucket.lmb.id
  key =  var.lambda_api_zip
  source = "${var.lambda_api_zip_dir}/${var.lambda_api_zip}"
  etag = filemd5("${var.lambda_api_zip_dir}/${var.lambda_api_zip}")
}


resource "aws_s3_bucket_object" "translate_upload" {
  bucket = aws_s3_bucket.lmb.id
  key =  var.lambda_translate_zip
  source = "${var.lambda_translate_zip_dir}/${var.lambda_translate_zip}"
  etag = filemd5("${var.lambda_translate_zip_dir}/${var.lambda_translate_zip}")
}

resource "aws_s3_bucket_object" "sentiment_upload" {
  bucket = aws_s3_bucket.lmb.id
  key =  var.lambda_sentiment_zip
  source = "${var.lambda_sentiment_zip_dir}/${var.lambda_sentiment_zip}"
  etag = filemd5("${var.lambda_sentiment_zip_dir}/${var.lambda_sentiment_zip}")
}

resource "aws_s3_bucket_object" "train_data_upload" {
  bucket = aws_s3_bucket.train.id
  key =  var.training_data
  source = "${var.training_data_dir}/${var.training_data}"
  etag = filemd5("${var.training_data_dir}/${var.training_data}")
}
