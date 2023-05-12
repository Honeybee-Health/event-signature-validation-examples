"""Example illustrating how to validate webhook event signature in Python"""
import hashlib
import hmac
import json
from base64 import b64encode
import urllib.parse

request = {
    "url": "https://webhook.site/bafb9675-e60a-4db3-ab3c-377061a12ed5",
    "method": "POST",
    "body": {
        "event_id": "5d62dca6-1ce3-4807-afc6-8cfaa47577c5",
        "event_type": "RX_RECEIVED",
        "patient_id": "v3y4od",
        "medication_requests": [
            {
                "id": 82,
                "prescription_id": 82,
                "active": True,
                "patient_id": "v3y4od",
                "prescriber_name": "Dr. Jane Foster",
                "receive_date": "2023-05-10T16:16:58.469+00:00",
                "drug_name": "ATORVASTATIN 10MG TABLET",
                "ndc": "5976201551",
                "sig_text": "Take 1 tablet daily",
                "written_qty": 90,
                "days_supply": None,
                "refills_left": 2,
                "expire_date": "2023-06-12T14:15:22Z",
                "drug_schedule": 4,
            }
        ],
    },
}

CLIENT_SECRET = "[CLIENT_SECRET_HERE]"
HASHED_CLIENT_SECRET = hashlib.sha256(CLIENT_SECRET.encode("utf-8")).hexdigest()
request_str = json.dumps(request["body"], separators=(",", ":"))

base = urllib.parse.quote_plus(
    request["method"] + request["url"] + request_str, safe=""
)
digest = hmac.digest(
    HASHED_CLIENT_SECRET.encode("utf-8"), base.encode("utf-8"), hashlib.sha1
)

print(b64encode(digest + b"\n").decode())
