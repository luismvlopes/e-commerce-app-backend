# E-commerce Application

## Description

This is an e-commerce application developed in Java with Spring Boot. It allows users to shop online, manage products, and handle orders. The application features authentication and authorization using Spring Security, with email validation for new user registration.

## Features

- **Authentication and Authorization:**
    - User registration with email validation.
    - User login to access protected resources.
    - JWT token generation for authenticated sessions.
- **Product Management:**
    - CRUD operations for products (Create, Read, Update, Delete).
- **Order Management:**
    - Creating and viewing orders.
- **RESTful Endpoints:**
    - Controllers for authentication, products, and orders.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Hibernate
- JPA (Java Persistence API)
- SQL Server
- Maven
- Git

## Requirements

- Java 11 or higher
- Maven 3.6 or higher
- SQL Server
- An SMTP server for sending email validations

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/ecommerce-app.git
    cd ecommerce-app
    ```

2. Configure the database in `application.properties`:
    ```properties
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.datasource.url=jdbc:sqlserver://your_db_host;databaseName=your_db_name;encrypt=true;trustservercertificate=true

    spring.jpa.hibernate.ddl-auto=update
    ```

3. Configure the email service in `application.properties`:
    ```properties
    spring.mail.host=localhost
    spring.mail.port=25
    email.from=no-reply@ecommerce.com
    ```

4. Compile and run the application:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## Endpoints REST

### AuthenticationController

- `POST /api/auth/register`: Registers a new user and sends a validation email.
- `POST /api/auth/verify`: Verifies the token sent via email.
- `POST /api/auth/login`: Authenticates the user and returns a JWT token.

### ProductController

- `GET /api/products`: Lists all products.
- `GET /api/products/{id}`: Retrieves a specific product.
- `POST /api/products`: Adds a new product.
- `PUT /api/products/{id}`: Updates an existing product.
- `DELETE /api/products/{id}`: Deletes a product.

### OrderController

- `GET /api/orders`: Lists all orders for the authenticated user.
- `POST /api/orders`: Creates a new order.

## Usage

1. **Registration and Validation:**
    - Register a new user using the `/api/auth/register` endpoint.
    - Check your email to validate the account using the token sent.

2. **Login:**
    - Log in using the `/api/auth/login` endpoint to obtain a JWT token.

3. **Access Protected Endpoints:**
    - Use the JWT token in the header of requests to access protected endpoints (Products and Orders).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
