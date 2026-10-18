# BankingApp — Spring Boot REST API

A backend banking application built using Java, Spring Boot, Spring Data JPA, and MySQL. It provides REST APIs for managing bank accounts and performing basic banking transactions.

## Features

- Create a bank account
- Retrieve account details by ID
- Retrieve all accounts
- Deposit money
- Withdraw money
- Delete an account
- Input validation and centralized exception handling
- REST API documentation with Swagger UI
- Unit tests and integration tests

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **API:** REST
- **Testing:** JUnit, Mockito, Spring Boot Test, MockMvc

## Getting Started

### Prerequisites

- Java JDK compatible with the project
- MySQL Server
- Maven, or the included Maven Wrapper

### Database Setup

Create a MySQL database:

```sql
CREATE DATABASE banking_app;
```

Configure your database URL and username in `src/main/resources/application.properties`. Store your database password in the `DB_PASSWORD` environment variable instead of hardcoding it.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_app
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

Set `DB_USERNAME` and `DB_PASSWORD` in your local environment before starting the application. Never commit real credentials to GitHub.

### Run the Application

On Windows, from the project root:

```bash
mvnw.cmd spring-boot:run
```

The application will normally start at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/accounts` | Create an account |
| GET | `/api/accounts/{id}` | Get account by ID |
| GET | `/api/accounts` | Get all accounts |
| PUT | `/api/accounts/{id}/deposit` | Deposit money |
| PUT | `/api/accounts/{id}/withdraw` | Withdraw money |
| DELETE | `/api/accounts/{id}` | Delete an account |

### Example Request: Create Account

```http
POST /api/accounts
Content-Type: application/json
```

```json
{
  "accountHolderName": "Rahul Sharma",
  "balance": 1000
}
```

Use the application's actual DTO fields and validation rules when testing requests.

## API Documentation

If Swagger UI is enabled, open:

`http://localhost:8080/swagger-ui/index.html`

## Testing

The project includes unit tests, controller tests, repository integration tests, and API integration tests. Run the test suite with:

```bash
mvnw.cmd test
```

## Future Improvements

- Authentication and authorization
- Role-based access control
- Transaction history
- Docker deployment
- CI/CD pipeline

## Author

**Sachuarpit**

GitHub: https://github.com/Sachuarpit

## Postman Collection

Use the Postman collection below to import and test the BankingApp REST APIs.

**[View / Download Postman Collection](https://github.com/Sachuarpit/BankingApp-Spring-Boot/blob/ef9af1f8baf1bd092989876f76b48d1db9baf32e/BankingApp%20API.postman_collection.json)**

### How to Run API Tests

1. Download the Postman collection JSON file.
2. Open Postman and select **Import**.
3. Choose the downloaded JSON file.
4. Start the BankingApp Spring Boot application.
5. Send requests to `http://localhost:8080`.

The collection includes requests for account creation, account retrieval, deposits, withdrawals, and account deletion.

