# Fairbrand

## How to run in local?

If you're running in the first time, do this:
```shell
make compile
docker build -t coltip:1.0.0 .
make up
```

Otherwise:

```shell
make run-solution
```

## API Documentation

1. Api documentation's UI is on **`<server-url>/api/swagger-ui/index.html`**

2. JSON Representation is on **`<server-url>/api/v3/api-docs`**

## First admin account

While you run the app for the first time (only the first time).
You'll see in the logs a generated password for the admin below the **Info** log: `Admin has been created`

The message you will see:
```
Your password is: XXXXXXXXXXXXXXXXXXXXXXXXXXX
NB: This password is only available once.
No other password will be sent.
Once you're connected, change your password!
```
