resource "aws_cognito_user_pool" "userPool" {
  name = "todoPool"

  username_attributes = ["email"]
  auto_verified_attributes = ["email"]
  email_verification_subject = "Your verification code"
  email_verification_message = "Your verification code is {####}."
  schema {
    attribute_data_type = "String"
    name = "email"
    mutable = true
    required = true
    developer_only_attribute = false

    number_attribute_constraints {
      min_value = 1
      max_value = 256
    }
  }

  admin_create_user_config {

    invite_message_template {
      email_message = "Your username is {username} and temporary password is {####}."
      email_subject = "Your temporary password"
      sms_message = "You are authenticated with {username} and {####}"
    }

    allow_admin_create_user_only = true
  }

}

resource "aws_cognito_user_pool_client" "userPoolClient" {
  name = "userPoolClient"
  user_pool_id = aws_cognito_user_pool.userPool.id
  generate_secret = false
  callback_urls = ["https://s3-eu-west-1.amazonaws.com/td-frontend/index.html"]
}

resource "aws_cognito_user_pool_domain" "userPoolDomain" {
  domain = "td-frontend"
  user_pool_id = aws_cognito_user_pool.userPool.id
}


resource "aws_cognito_identity_pool" "identityPool" {
  identity_pool_name = "identityPool"
  allow_unauthenticated_identities = false
  cognito_identity_providers {
    client_id = aws_cognito_user_pool_client.userPoolClient.id
    provider_name = "cognito-idp.us-east-1.amazonaws.com/${aws_cognito_user_pool.userPool.id}"
  }
}

resource "aws_cognito_identity_pool_roles_attachment" "poolAtt" {
  identity_pool_id = aws_cognito_identity_pool.identityPool.id
  roles = {
        authenticated: aws_iam_role.cognito.arn
  }
}
