# Store REST API

A production-style Spring Boot backend for an online store, built to manage catalog, cart, checkout, authentication, and order flow with modern Java tooling.

## Overview

Store REST API is a full-featured backend service for an online store. It supports the core operations needed to run a modern commerce workflow, including:

- User registration, authentication, and profile management
- Product catalog management and category-based filtering
- Shopping cart creation and item management
- Order creation and retrieval
- Stripe-based payment checkout
- MySQL persistence with Flyway migrations
- Swagger/OpenAPI documentation
- Security using JWT and Spring Security

This project is designed as a practical backend implementation for learning and building real-world Spring Boot APIs.

## Features

- User registration and login
- JWT-based authentication and authorization
- Password change support
- Product CRUD operations
- Category-based product search and filtering
- Cart creation, update, and removal flows
- Order processing and order status tracking
- Stripe checkout integration
- Flyway-based database migrations
- REST API documentation with Swagger UI
- Thymeleaf-based simple UI entry page

## Tech Stack

- Java 26
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (jjwt)
- MySQL
- Flyway
- Lombok
- MapStruct
- Spring Validation
- Stripe Java SDK
- Springdoc OpenAPI
- Maven

## Project Structure

```text
Store-REST-API/
├── .mvn/
├── .run/
├── src/
│   ├── main/
│   │   ├── java/com/yugal/store/
│   │   │   ├── admin/
│   │   │   ├── auth/
│   │   │   ├── carts/
│   │   │   ├── commons/
│   │   │   ├── orders/
│   │   │   ├── payments/
│   │   │   ├── products/
│   │   │   ├── users/
│   │   │   └── StoreApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       ├── templates/
│   │       └── application.yaml
│   └── test/
├── .env.example
├── .gitignore
├── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
├── script.sql
├── .gitattributes
└── LICENSE (if added later)
```

## Authentication

The application uses JWT-based authentication for protected endpoints.

Create a `.env` file from `.env.example`:

```dotenv
JWT_SECRET=your_secure_secret
STRIPE_SECRET_KEY=your_stripe_secret
STRIPE_WEBHOOK_SECRET_KEY=your_webhook_secret
```

## Database Configuration

The project uses MySQL with Flyway for schema management.

Example configuration from `src/main/resources/application.yaml`:

```yaml
spring:
  config:
    import: optional:file:.env[.properties]
  application:
    name: store
  datasource:
    url: jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true
    username: yugal
    password: 4102000
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
  jwt:
    secret: ${JWT_SECRET}
    accessTokenExpiration: 900
    refreshTokenExpiration: 604800
  stripe:
    secretKey: ${STRIPE_SECRET_KEY}
    webhookSecretKey: ${STRIPE_WEBHOOK_SECRET_KEY}
websiteUrl: http://localhost:4242
```

## Running the Project

### Prerequisites

- Java 26
- Maven
- MySQL running locally

### Clone the repository

```bash
git clone https://github.com/yugalteotia/Store-REST-API.git
cd Store-REST-API
```

### Start the application

```bash
./mvnw spring-boot:run
```

The app runs at:

```text
http://localhost:8080
```

### Swagger UI

API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

## Main Modules

### Users
- Register users
- Fetch users
- Update profile details
- Change password
- Role-based access handling

### Products
- Create and update products
- Fetch all products or category-filtered products
- View a single product
- Delete products

### Carts
- Create a cart
- Add product items
- Update item quantity
- Remove individual items
- Clear the cart

### Orders
- Create orders from cart data
- Retrieve order details
- Track payment status

### Payments
- Stripe checkout session creation
- Webhook request handling
- Payment result processing

## API Overview

### Auth
- `POST /auth/login`
- `POST /auth/register`

### Users
- `GET /users`
- `GET /users/{id}`
- `POST /users`
- `PUT /users/{id}`
- `DELETE /users/{id}`
- `POST /users/{id}/change-password`

### Products
- `GET /products`
- `GET /products?categoryId={id}`
- `GET /products/{id}`
- `POST /products`
- `PUT /products/{id}`
- `DELETE /products/{id}`

### Carts
- `POST /carts`
- `GET /carts/{cartId}`
- `POST /carts/{cartId}/items`
- `PUT /carts/{cartId}/items/{productId}`
- `DELETE /carts/{cartId}/items/{productId}`
- `DELETE /carts/{cartId}/items`

### Orders
- `GET /orders`
- `GET /orders/{id}`
- `POST /orders`

### Payments
- `POST /checkout`
- `POST /webhook`

## Notes

This project is built as a hands-on backend development exercise and demonstrates real-world patterns such as:

- Layered architecture
- DTO-based REST design
- JPA persistence with relational data models
- Validation and exception handling
- JWT security
- Payment integration
- API documentation

## License

This project is intended for learning, personal development, and backend experimentation.

## Contact

If you want to contribute or discuss improvements, feel free to reach out through the GitHub repository.
