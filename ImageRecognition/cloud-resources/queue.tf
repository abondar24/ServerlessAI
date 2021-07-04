resource "aws_sqs_queue" "img_rec_queue" {
  name = "ImgRecQueue"
  visibility_timeout_seconds = 300
}

resource "aws_sqs_queue" "img_rec_anl_queue" {
  name = "ImgRecAnlQueue"
  visibility_timeout_seconds = 300
}
