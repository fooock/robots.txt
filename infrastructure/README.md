# Infra

This document explains how this project was deployed.

# AWS Lambda

To update expired URLs from database, we use a scheduled *Lambda* function. This function
is executed every hour. To deploy it, create a ``~/.aws/credentials`` with a new profile called
``serverless`` and execute the following command to deploy it to AWS:

```
$ serverless deploy --aws-profile serverless
```
