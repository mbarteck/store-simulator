# Store Simulator

This is a simple backend application written in Java that simulates the behavior of an online store.
The application provides REST APIs to handle a user's shopping cart and payment transactions. It
utilizes Kafka as a messaging system and H2 database for data storage.

## Designed flow

- The checkout API receives a request.
- A new payment is created and stored in the database.
- Within the same transaction, two outbox events are saved in the database:
    - the first event contains details of the transaction,
    - the second event contains details of the email.
- The outbox event publisher retrieves the events from the database and publishes them to 2 separate
  topics in the Kafka queueing system.
- The payment consumer receives the relevant event from the queue and attempts the payment with the
  mock payment provider.
- Depending on the outcome from payment provider, the payment consumer updates the status of the
  payment in the database.
- Within the same transaction, one outbox event with email details is saved in the database.
- Simultaneously, the email consumer retrieves the relevant events from the queue and simulates
  sending the email (printed to the console).

This flow ensures that the payment and email events are processed atomically and consistently,
maintaining data integrity and reliable message delivery.

## Assumptions:

To ensure the simplicity of the application, the following assumptions have been made:

1. Stock Verification: The list of items provided to the checkout API is assumed to have been
   previously checked for availability in the stock. Therefore, no additional stock checks are
   performed within the application. In a more advanced version of the application, this
   verification could be implemented by interacting with a warehouse API to reserve the items.

2. User Identification: The application does not store user information. It relies on the assumption
   that the email address provided in the request uniquely identifies the user. This email address
   can be forwarded to the payment provider, which is responsible for recognizing the user based on
   the provided email address.

3. Unique Payment Identification: Each payment made through the application is assigned a unique ID.
   The payment provider is designed to ignore payments with the same ID that have already been
   processed. This ensures that duplicate payments are not processed multiple times, providing data
   consistency and avoiding unintended financial transactions.

These assumptions help simplify the application logic, delegate certain responsibilities to external
systems, and ensure the uniqueness and integrity of payment transactions.

## Prerequisites

Make sure you have the following software installed on your system:

- Docker
- Java 17
- Maven

## Setup

- Enter project directory
- Start a Kafka instance defined in `docker-compose.yml` file:

```bash
docker-compose up -d
```

- Build project:

```bash
mvn clean install
```

- Run jar:

```bash
java -jar target/store-simulator-0.0.1-SNAPSHOT.jar
```

## Usage

### Checkout API

The application provides a single API endpoint for processing an order by receiving a cart request.
The endpoint is exposed at `/checkout` and accepts a POST request with the following JSON payload:

### Request

```json
{
  "userEmail": "string",
  "items": [
    {
      "productId": "integer",
      "price": "number",
      "quantity": "integer"
    },
    ...
  ]
}
```

The `userEmail` field should contain the email address of the user placing the order. The `items`
field should be an array of objects representing the `items` in the cart. Each item object should
have the `productId` (integer), `price` (number), and `quantity` (integer) fields specifying the
details of the item.

### Example request

You can use the following cURL command to send a sample request to the application:

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
}' http://localhost:8081/checkout
```

Replace http://localhost:8081 with the actual URL of the application.

### Expected output

In the console you should see:

```
### MAIL SERVICE ###
Sending email to: example@example.com
Subject: Payment has been created
Body: Payment with ID bdb679c8-12da-4de3-b256-20ff9e59a257 has been created
Email sent successfully!

### MAIL SERVICE ###
Sending email to: example@example.com
Subject: Payment succeeded
Body: Payment with ID bdb679c8-12da-4de3-b256-20ff9e59a257 succeeded
Email sent successfully!

```

This indicates that the designed flow has taken place.
