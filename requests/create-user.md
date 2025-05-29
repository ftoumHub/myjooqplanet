### POST request to create a customer

````bash
curl -X POST http://localhost:8181/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "country": "Belgium"
  }'
````

````bash
curl -X GET http://localhost:8181/customers \
  -H "Content-Type: application/json"
````