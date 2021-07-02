resource "aws_s3_bucket" "b" {
  bucket = "img-rec"
  acl = "public-read-write"
  tags = {
    Name = "Image rec bucket"
    Environment = "dev"
  }

}
