# E-Commerce Backend

A backend REST API for an e-commerce application built with **Java and Spring Boot**.
The project follows a layered architecture with separate controllers, services, repositories, DTOs, and entities.

## 🚀 Features

* User management
* Address management
* Category management
* Product management
* Inventory management
* Shopping cart
* Order management
* Order items
* Payment management
* Product reviews
* Wishlist
* Input validation
* Global exception handling
* JPA/Hibernate database integration

## 🛠️ Tech Stack

* **Java 24**
* **Spring Boot 4**
* **Spring Data JPA**
* **Hibernate**
* **MySQL 8**
* **Maven**
* **Lombok**
* **Postman**
* **Git & GitHub**

## 🏗️ Project Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

DTOs are used to transfer data between the API and client instead of exposing entities directly.

### Package Structure

```text
src/main/java/com/shashank/ecommerce
│
├── cart
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── category
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── inventory
│   ├── dto
│   ├── entity
│   └── repository
│
├── order
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── payment
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── product
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── review
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── wishlist
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
└── exception
```

## 📦 Main Modules

| Module    | Description                        |
| --------- | ---------------------------------- |
| User      | Customer and admin management      |
| Address   | User shipping addresses            |
| Category  | Product categorization             |
| Product   | Product creation and management    |
| Inventory | Product stock management           |
| Cart      | Shopping cart and cart items       |
| Order     | Order creation and order items     |
| Payment   | Payment records and payment status |
| Review    | Product reviews and ratings        |
| Wishlist  | Wishlist and wishlist items        |

## 🗄️ Database

The application uses **MySQL**.

Database configuration is kept outside the source code using environment variables.

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Set these environment variables before running the application.

Example:

```text
DB_USERNAME=root
DB_PASSWORD=your_password
```

> Never commit real database credentials to GitHub.

## ▶️ Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/Shashank907/ecommerce-spring-boot.git
```

### 2. Navigate into the project

```bash
cd ecommerce-spring-boot
```

### 3. Create the MySQL database

```sql
CREATE DATABASE ecommerce;
```

### 4. Configure environment variables

Set:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

### 5. Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## 🔌 API Examples

Some of the available endpoints include:

### Products

```http
GET    /products
POST   /products
GET    /products/{id}
PUT    /products/{id}
DELETE /products/{id}
```

### Cart

```http
POST   /users/{userId}/cart
GET    /users/{userId}/cart
POST   /users/{userId}/cart/items
DELETE /users/{userId}/cart/items/{productId}
```

### Wishlist

```http
POST   /users/{userId}/wishlist
GET    /users/{userId}/wishlist
POST   /users/{userId}/wishlist/items/{productId}
GET    /users/{userId}/wishlist/items
DELETE /users/{userId}/wishlist/items/{productId}
```

### Reviews

```http
POST   /users/{userId}/products/{productId}/reviews
GET    /users/{userId}/products/{productId}/reviews
DELETE /users/{userId}/products/{productId}/reviews/{reviewId}
```

## 🧪 API Testing

The APIs can be tested using **Postman**.

Typical development flow:

```text
Create Category
      ↓
Create Product
      ↓
Add Inventory
      ↓
Create User
      ↓
Create Address
      ↓
Add Product to Cart
      ↓
Create Order
      ↓
Create Payment
      ↓
Add Review / Wishlist
```

## 🔒 Production Improvements

The current project is being developed toward a production-ready backend.

Planned improvements include:

* BCrypt password hashing
* JWT authentication
* Role-based authorization
* Improved exception handling
* BigDecimal for monetary values
* Additional validation
* Unit and integration testing
* Docker containerization
* React frontend
* API documentation
* Cloud deployment

## 📌 Project Status

**Backend:** Core modules completed

**Authentication:** Planned

**Frontend:** Planned

**Docker:** Planned

**Deployment:** Planned

## 👨‍💻 Author

**Shashank Shekhar Pandey**

B.Tech Computer Science & Engineering

GitHub:
https://github.com/Shashank907

## 📄 License

This project is currently intended for learning, portfolio development, and demonstration purposes.
