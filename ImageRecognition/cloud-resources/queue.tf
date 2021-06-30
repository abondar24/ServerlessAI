resource "aws_sqs_queue" "img_rec_queue" {
  name = "ImgRecQueue"
}

resource "aws_sqs_queue" "img_rec_anl_queue" {
  name = "ImgRecAnlQueue"
}
