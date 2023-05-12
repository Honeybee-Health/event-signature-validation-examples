require 'cgi'
require 'base64'
require 'openssl'
require 'json'

request = {
    url: "https://webhook.site/bafb9675-e60a-4db3-ab3c-377061a12ed5",
    method: "POST",
    body: {
        "event_id": "5d62dca6-1ce3-4807-afc6-8cfaa47577c5",
        "event_type": "RX_RECEIVED",
        "patient_id": "v3y4od",
        "medication_requests": [
            {
                "id": 82,
                "prescription_id": 82,
                "active": true,
                "patient_id": "v3y4od",
                "prescriber_name": "Dr. Jane Foster",
                "receive_date": "2023-05-10T16:16:58.469+00:00",
                "drug_name": "ATORVASTATIN 10MG TABLET",
                "ndc": "5976201551",
                "sig_text": "Take 1 tablet daily",
                "written_qty": 90,
                "days_supply": nil,
                "refills_left": 2,
                "expire_date": "2023-06-12T14:15:22Z",
                "drug_schedule": 4
            }
        ]
    }
}

CLIENT_SECRET = "[CLIENT_SECRET_HERE]"
HASHED_CLIENT_SECRET = Digest::SHA256.hexdigest(CLIENT_SECRET)

base = CGI.escape(request[:method] + request[:url] + (request[:body].to_json))
digest = Base64.encode64("#{OpenSSL::HMAC.digest('sha1', HASHED_CLIENT_SECRET, base)}\n")

puts digest
