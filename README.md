# Store REST API

A personal learning project built with Spring Boot to explore REST API development, database persistence, validation, and backend architecture for an online store.

This project is not a production-ready e-commerce system, but it is a practical implementation of core concepts I wanted to practice while learning: entity modeling, repositories, DTOs, controllers, validation, database migration, and API documentation.

## Features

- User management with create, read, update, and delete operations
- Product catalog with category-based filtering
- Cart creation and cart item management
- Quantity updates and cart clearing
- Validation for request payloads
- MySQL database integration with Flyway migrations
- Swagger/OpenAPI documentation
- Simple Thymeleaf home page for a basic UI entry point

## Tech Stack

- Java 26
- Spring Boot 4
- Spring Web
- Spring Data JPA
- MySQL
- Flyway
- Lombok
- MapStruct
- Spring Validation
- Springdoc OpenAPI
- Maven

## Project Structure

```text
store-rest-api/
├── src/
│   ├── main/
│   │   ├── java/com/yugal/store/
│   │   │   ├── controllers/
│   │   │   ├── dtos/
│   │   │   ├── entities/
│   │   │   ├── exceptions/
│   │   │   ├── mappers/
│   │   │   ├── repositories/
│   │   │   ├── services/
│   │   │   ├── validation/
│   │   │   └── StoreApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       ├── templates/
│   │       └── application.yaml
│   └── test/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── script.sql
├── README.md
└── .gitignore
```

## API Overview

### Users

- `GET /users` — get all users
- `GET /users/{id}` — get a user by ID
- `POST /users` — register a user
- `PUT /users/{id}` — update user details
- `DELETE /users/{id}` — delete a user
- `POST /users/{id}/change-password` — change a user's password

### Products

- `GET /products` — get all products
- `GET /products?categoryId={id}` — get products by category
- `GET /products/{id}` — get a product by ID
- `POST /products` — create a product
- `PUT /products/{id}` — update a product
- `DELETE /products/{id}` — delete a product

### Carts

- `POST /carts` — create a cart
- `GET /carts/{cartId}` — get cart details
- `POST /carts/{cartId}/items` — add a product to cart
- `PUT /carts/{cartId}/items/{productId}` — update item quantity
- `DELETE /carts/{cartId}/items/{productId}` — remove an item
- `DELETE /carts/{cartId}/items` — clear the cart

## Database

The application uses MySQL and Flyway for schema management.

Default configuration is defined in `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true
    username: yugal
    password: 4102000
```

Make sure MySQL is running locally and the database is available before starting the app.

## Running the Project

### Prerequisites

- Java 26
- Maven
- MySQL server running locally

### Clone the project

```bash
git clone https://github.com/yugalteotia/Store-REST-API.git
cd Store-REST-API
```

### Start the application

```bash
./mvnw spring-boot:run
```

The app will start on:

```text
http://localhost:8080
```

### Swagger UI

After the application starts, you can inspect the API docs here:

```text
http://localhost:8080/swagger-ui/index.html
```

## Database Migrations

Flyway migration files are stored in:

```text
src/main/resources/db/migration/
```

This helps keep the database schema versioned and consistent.

## Notes

This project was built as a hands-on learning exercise to understand how a real Spring Boot API is structured and how different components fit together. It includes practical features like DTO mapping, data validation, MySQL persistence, and RESTful endpoints.

There are still areas that could be improved for production use, such as authentication, role-based access control, more robust error handling, and better separation of business logic.

## License

This project is for learning and personal development purposes.

## Contact

If you want to connect or discuss improvements, feel free to reach out through GitHub.
