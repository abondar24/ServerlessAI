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

  cors_rule {
    allowed_methods = ["GET","PUT","POST","DELETE","HEAD"]
    allowed_origins = ["*"]
    max_age_seconds = 3000
  }

  website {
    index_document = "index.html"
    error_document = "index.html"
  }
}

resource "aws_s3_bucket_object" "frontend" {
  bucket = aws_s3_bucket.frb.id
  for_each = fileset("../Frontend","*")
  key = each.value
  source = "../Frontend/${each.value}"
  etag = filemd5("../Frontend/${each.value}")
}
