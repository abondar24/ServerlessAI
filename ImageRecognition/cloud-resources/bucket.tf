resource "aws_s3_bucket" "b" {
  bucket = "img-rec"
  acl = "private"

  tags = {
    Name = "Image rec bucket"
    Environment = "dev"
  }


}
