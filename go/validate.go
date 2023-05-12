package main

import (
	"crypto/hmac"
	"crypto/sha1"
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"net/url"
)

type RawRequest struct {
	Url    string
	Method string
	Body   string
}

type RequestBody struct {
	EventID            string              `json:"event_id"`
	EventType          string              `json:"event_type"`
	PatientID          string              `json:"patient_id"`
	MedicationRequests []MedicationRequest `json:"medication_requests"`
}

type MedicationRequest struct {
	ID             int         `json:"id"`
	PrescriptionID int         `json:"prescription_id"`
	Active         bool        `json:"active"`
	PatientID      string      `json:"patient_id"`
	PrescriberName string      `json:"prescriber_name"`
	ReceiveDate    string      `json:"receive_date"`
	DrugName       string      `json:"drug_name"`
	NDC            string      `json:"ndc"`
	SigText        string      `json:"sig_text"`
	WrittenQty     int         `json:"written_qty"`
	DaysSupply     interface{} `json:"days_supply"`
	RefillsLeft    int         `json:"refills_left"`
	ExpireDate     string      `json:"expire_date"`
	DrugSchedule   int         `json:"drug_schedule"`
}

func main() {
	request := RawRequest{
		Url:    "https://webhook.site/bafb9675-e60a-4db3-ab3c-377061a12ed5",
		Method: "POST",
		Body: `{
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
					"drug_schedule": 4
				}
			]
		}`,
	}

	clientSecret := "[CLIENT_SECRET_HERE]"
	hashedClientSecret := sha256.Sum256([]byte(clientSecret))
	hashedClientSecretStr := hex.EncodeToString(hashedClientSecret[:])

	flattenedBody, err := flattenJson(request.Body)
	if err != nil {
		panic(err)
	}

	base := url.QueryEscape(request.Method + request.Url + flattenedBody)
	hash := hmac.New(sha1.New, []byte(hashedClientSecretStr))
	hash.Write([]byte(base))
	digest := hash.Sum(nil)

	fmt.Println(base64.StdEncoding.EncodeToString(append(digest, '\n')))
}

func flattenJson(jsonStr string) (string, error) {
	var data RequestBody
	if err := json.Unmarshal([]byte(jsonStr), &data); err != nil {
		return "", err
	}

	body, err := json.Marshal(data)
	if err != nil {
		return "", err
	}

	return string(body), nil
}
