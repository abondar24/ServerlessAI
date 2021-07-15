resource "aws_dynamodb_table" "todo-dynamodb-table"{

  name           = var.dn_table
  billing_mode   = "PROVISIONED"
  read_capacity  = 20
  write_capacity = 20

  attribute {
    name = "id"
    type = "S"
  }

  hash_key = "id"


}
