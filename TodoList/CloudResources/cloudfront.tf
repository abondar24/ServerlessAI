
locals {
  s3_origin_id = "myS3Origin"
}

resource "aws_cloudfront_distribution" "fr_distribution" {
  enabled = false
  default_cache_behavior {
    allowed_methods = ["GET", "HEAD", "OPTIONS", "PUT", "POST", "PATCH", "DELETE"]
    cached_methods = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = var.origin_id
    viewer_protocol_policy = "allow-all"
    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }
  }
  origin {
    domain_name = aws_s3_bucket.frb.bucket_domain_name
    origin_id =  var.origin_id

  }
  restrictions {
    geo_restriction {
      restriction_type = "whitelist"
      locations        = ["US", "CA", "GB", "DE"]
    }
  }
  viewer_certificate {
    cloudfront_default_certificate = true

  }
}
