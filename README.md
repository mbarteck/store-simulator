# store-simulator

# Setup

```bash
docker-compose up -d
```

# Example request

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "userEmail": "example@example.com",
  "items": [
    {
      "productId": 1,
      "price": 10.99,
      "quantity": 2
    },
    {
      "productId": 2,
      "price": 5.99,
      "quantity": 1
    },
    {
      "productId": 3,
      "price": 7.5,
      "quantity": 3
    },
    {
      "productId": 4,
      "price": 12.49,
      "quantity": 1
    }
  ]
}' http://localhost:8080/checkout
```
