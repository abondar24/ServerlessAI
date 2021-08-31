resource "aws_s3_bucket" "idb" {
  bucket = var.bucket_name
  acl = "public-read"
  tags = {
    Name = "Identity analysis bucket"
    Environment = "dev"
  }

  versioning {
    enabled = true
  }

  cors_rule {
    allowed_methods = ["GET","PUT","POST","DELETE","HEAD"]
    allowed_origins = ["*"]
    allowed_headers = ["*"]
    max_age_seconds = 3000
  }

  website {
    index_document = "index.html"
    error_document = "index.html"
  }
}
