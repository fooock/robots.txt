# Infra

[![serverless](http://public.serverless.com/badges/v3.svg)](http://www.serverless.com)

This document explains how this project was deployed.

# AWS Lambda

To update expired URLs from database, we use a scheduled *Lambda* function. This function
is executed every hour. To deploy it, create a ``~/.aws/credentials`` with a new profile called
``serverless`` and execute the following command to deploy it to AWS:

```
$ serverless deploy --stage dev
```
