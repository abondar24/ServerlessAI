
resource "aws_s3_bucket_object" "feedback_api_upload" {
  bucket = aws_s3_bucket.lmb.id
  key =  var.lambda_api_zip
  source = "${var.lambda_api_zip_dir}/${var.lambda_api_zip}"
}


resource "aws_s3_bucket_object" "translate_upload" {
  bucket = aws_s3_bucket.lmb.id
  key =  var.lambda_translate_zip
  source = "${var.lambda_translate_zip_dir}/${var.lambda_translate_zip}"
}
