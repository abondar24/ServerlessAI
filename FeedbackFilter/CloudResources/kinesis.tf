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
