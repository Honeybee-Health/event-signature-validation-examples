const crypto = require('crypto');
const querystring = require('querystring');

const request = {
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
        "days_supply": null,
        "refills_left": 2,
        "expire_date": "2023-06-12T14:15:22Z",
        "drug_schedule": 4,
      }
    ],
  },
};

const CLIENT_SECRET = "[CLIENT_SECRET_HERE]";
const HASHED_CLIENT_SECRET = crypto.createHash('sha256').update(CLIENT_SECRET).digest('hex');
const requestStr = JSON.stringify(request.body);

const base = querystring.escape(request.method + request.url + requestStr).replace(/%20/g, '+');
const hmac = crypto.createHmac('sha1', HASHED_CLIENT_SECRET);
hmac.update(base);
const digest = Buffer.from(hmac.digest('binary') + '\n', 'binary').toString('base64')

console.log(digest);
