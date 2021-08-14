resource "aws_s3_bucket" "dtb" {
  bucket = var.dt_bucket
  acl = "public-read"
  tags = {
    Name = "TodoList data bucket"
    Environment = "dev"
  }

  cors_rule {
    allowed_methods = ["GET","PUT","POST","DELETE","HEAD"]
    allowed_origins = ["*"]
    allowed_headers = ["*"]
    max_age_seconds = 3000
  }


}

resource "aws_s3_bucket" "frb" {
  bucket = var.fr_bucket
  acl = "public-read"
  tags = {
    Name = "TodoList frontend bucket"
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

