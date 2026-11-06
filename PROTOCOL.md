As I went to official portal at : https://urja-ops.flockenergy.tech/

First I saw a clean authentication UI. 
As of writing this MD, it was asking email and password.(credentials which were shared to us personally).

For sake of simplicity I am having chrome devtools open to inspect the network requests.

Then, dashboard opened with default(page 1) of meters data. (containing few fields of meter scattered in multiple columns across meters that could accompany within that single page).
Then I tried hitting the next page for meters search. 
Then I tried the search input field for the meters. (Tried inputting both meterId and serialNo).

Also, went to the transformers section on the navbar. 

It displayed the data in same fashion in a paginated grid, as it was for meters. (just with different attributes).
It had a button to export all meters data to JSON there.

Also signout was there all the time on navbar in top-right.

Before writing out the exact details of the network calls, I would like to list few observation:

### Observations:

- On Session Expired: Any further request from main legacy portal is denied as unauthorised. Implies, carefully handled.
- Meter Search: search got triggered for every character. 
(it seems fine for this particular product, otherwise industry standard seems to be using at least 3 characters to trigger).
- Export Meter: Request was done following a get key request. A public signing key got as response from server is used to sign some "magic-message" and used to generate 2 keys which are sent to export request in headers.
- Meter Search is full text search. that is each character added or any string queried is searched in both meterId and serialNo attributes in all possible entries with full text (written in SQL as: %q%). 



### All the APIS: 

1. Search Meters:
  METHOD: GET
  URL: https://urja-ops.flockenergy.tech/portal/meters/search?q={{q}}&page={{page}}
  Query Parameters:
      - q: input search query (By meterId or serialNo, full text search)
      - page: page number
  Cookie: __Secure-better-auth.session_token=ACCESS_COOKIE; Path=/; Secure; HttpOnly;

Response:
```
{
  "data": [
  {
    "meterId": "string",
    "serialNo": "string",
    "make": "string",
    "phaseType": "string",
    "installStatus": "string",
    "dtCode": "string"
  }
]
}
```

2. List Transformers: 

  METHOD: GET
  URL: https://urja-ops.flockenergy.tech/portal/dts?page={{page}}
  Query Parameters:
      - page: page number
  Cookie: __Secure-better-auth.session_token=ACCESS_COOKIE; Path=/; Secure; HttpOnly;

Response:
```
{
  "data": [
  {
    "code": "string",
    "name": "string",
    "feederCode": "string",
    "capacityKva": "string"
  }
]
}
```


3. Export Meters:

    METHOD: GET
  URL: https://urja-ops.flockenergy.tech/portal/export?page=1
  Headers:
     - x-signature: GENERATED_SIGNATURE
     - x-timestamp: TIMESTAMP (epoch)
 Cookie: __Secure-better-auth.session_token=ACCESS_COOKIE; Path=/; Secure; HttpOnly;
   

Response:
```
{
  "data":[
  {
    "meterId": "string",
    "serialNo": "string",
    "make": "string",
    "phaseType": "string",
    "installStatus": "string",
    "installType": "string",
    "build": "string",
    "dtCode": "string",
    "hierarchy": {
      "zone": {
        "name": "string",
        "code": "string"
      },
      "circle": {
        "name": "string",
        "code": "string"
      },
      "division": {
        "name": "string",
        "code": "string"
      },
      "subDivision": {
        "name": "string",
        "code": "string"
      },
      "subStation": {
        "name": "string",
        "code": "string"
      },
      "feeder": {
        "name": "string",
        "code": "string"
      },
      "dt": {
        "name": "string",
        "code": "string"
      }
    },
    "geo": {
      "lat": "string",
      "lng": "string"
    }
  }
]
}
```

4. Get Export Signing key:

Method: GET
URL: https://urja-ops.flockenergy.tech/portal/keys
Cookie: __Secure-better-auth.session_token=ACCESS_COOKIE; Path=/; Secure; HttpOnly;

response: 
```
{
"data":{"signingSecret":"SIGNING_SECRET_KEY"}
}
```

----

5. Login to legacy portal:

Method: POST
URL: https://urja-ops.flockenergy.tech/login
Headers:
 - content-type: application/x-www-form-urlencoded
 - origin: https://urja-ops.flockenergy.tech
Body: email and password

Response: 

```
{"type":"redirect","status":303,"location":"/meters"}

With Cookie set.
```
6. Signing out of legacy portal:

Method: POST
URL: https://urja-ops.flockenergy.tech/api/auth/sign-out
Headers:
 - origin: https://urja-ops.flockenergy.tech
   
Response: {"success": true}


