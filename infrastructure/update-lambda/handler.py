from botocore.vendored import requests
import os


URL = "https://api.robotstxt.io/v1/update?size=100"

def update_expired(event, context):
    header_name = os.environ['REQUEST_AUTH_HEADER']
    header_value = os.environ['REQUEST_AUTH_VALUE']

    headers = {header_name : header_value}
    requests.put(URL, headers=headers)
