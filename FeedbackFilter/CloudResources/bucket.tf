resource "aws_s3_bucket" "lmb" {
  bucket = var.lambda_bucket
  acl = "public-read"
  tags = {
    Name = "Feedback filter lambdabucket"
    Environment = "dev"
  }

   versioning {
    enabled = true
  }

}
