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


resource "aws_s3_bucket" "train" {
  bucket = var.train_bucket
  acl = "public-read"
  tags = {
    Name = "Feedback filter training data"
    Environment = "dev"
  }

  versioning {
    enabled = true
  }

}

resource "aws_s3_bucket" "classifier" {
  bucket = var.result_bucket
  acl = "public-read"
  tags = {
    Name = "Feedback filter results"
    Environment = "dev"
  }

  versioning {
    enabled = true
  }

}
