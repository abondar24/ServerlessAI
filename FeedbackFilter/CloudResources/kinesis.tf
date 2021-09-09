resource "aws_kinesis_stream" "feedback_stream" {
  name = var.kinesis_stream
  shard_count = 1
  retention_period = 48

  shard_level_metrics = [
    "IncomingBytes",
    "OutgoingBytes",
  ]

  tags = {
    Environment = var.stage_test
  }
}

resource "aws_kinesis_stream" "sentiment_stream" {
  name = var.sentiment_stream
  shard_count = 1
  retention_period = 48

  shard_level_metrics = [
    "IncomingBytes",
    "OutgoingBytes",
  ]

  tags = {
    Environment = var.stage_test
  }
}

resource "aws_kinesis_stream" "classifier_stream" {
  name = var.classifier_stream
  shard_count = 1
  retention_period = 48

  shard_level_metrics = [
    "IncomingBytes",
    "OutgoingBytes",
  ]

  tags = {
    Environment = var.stage_test
  }
}
