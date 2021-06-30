provider "aws" {
  region = var.region
}

resource "aws_s3_bucket" "b" {
  bucket = "img-rec"
  acl = "private"

  tags = {
    Name = "Image rec bucket"
    Environment = "dev"
  }

  lifecycle {

  }

}

resource "aws_sqs_queue" "img_rec_queue" {
  name                        = "ImgRecQueue"
}

resource "aws_sqs_queue" "img_rec_anl_queue" {
  name                        = "ImgRecAnlQueue"
}
