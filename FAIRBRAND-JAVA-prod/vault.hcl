storage "file" {
  path = "/vault/data"
}

listener "tcp" {
  address = "0.0.0.0:8200"
  tls_disable = 1  # Set to 0 if using TLS
}

disable_mlock = true
ui = true