vonage request body:
{

    "country": "th",

    "appName": "lc_whatsapp",

    "clientReferenceNumber": "abea4897-374e-40a7-ae4f-7ade70c0818c",

    "messageRequest": {

    "to": "916290895296",

    "message_type": "custom",

    "custom": {

        "type": "template",

        "template": {

            "namespace": "9b6b4fcb_da19_4a26_8fe8_78074a91b584",

            "name": "verify",

            "language": {

                "policy": "deterministic",

                "code": "en"

            },

            "components": [

                {

                    "type": "body",

                    "parameters": [

                        {

                            "type": "text",

                            "text": "Vonage"

                        },

                        {

                            "type": "text",

                            "text": "12443"

                        },

                        {

                            "type": "text",

                            "text": "6"

                        }

                    ]

                }

            ]

        }

    }

    }

}

