# E-Commerce Backend

A production-oriented **e-commerce backend REST API** built with **Java 24 and Spring Boot 4**.

The project implements a complete customer-side e-commerce workflow including **authentication, products, cart, coupons, inventory reservation, orders, payments, refunds, reviews, wishlist, notifications, and order lifecycle management**.

The application follows a layered architecture with separate controllers, services, repositories, DTOs, and entities.

## 🚀 Features

### 👤 User & Authentication

* User registration and login
* JWT-based authentication
* Role-based authorization
* Customer and Admin roles
* Account status management
* BCrypt password encryption
* Protected API endpoints
* Custom authentication and access-denied handlers

### 🛍️ Product Management

* Product CRUD operations
* Category management
* Product search
* Product filtering
* Sorting
* Pagination
* Input validation

### 🛒 Shopping Cart

* Add products to cart
* Update cart quantity
* Remove cart items
* Cart total calculation
* Automatic cart clearing after successful payment

### 🎟️ Coupons

* Coupon creation and management
* Percentage/fixed discounts
* Coupon validation
* Minimum order requirements
* Expiry and active status
* Coupon usage tracking
* Admin coupon management

### 📦 Inventory

* Product stock management
* Available stock calculation
* Inventory reservation during checkout
* Stock consumption after successful payment
* Stock release after cancellation
* Stock restoration after refund
* Pessimistic locking for concurrent stock updates

### 📋 Orders

* Order creation
* Order items
* Shipping address snapshot
* Order ownership authorization
* Order cancellation
* Admin order management
* Order status lifecycle

Order lifecycle:

```text
PENDING
   ↓
CONFIRMED
   ↓
PROCESSING
   ↓
SHIPPED
   ↓
DELIVERED
```

Orders can also be cancelled according to the applicable business rules.

### 💳 Payments

* Payment creation
* Payment status management
* Payment success/failure handling
* Payment refunds
* Transaction IDs
* Payment idempotency
* BigDecimal-based monetary calculations

Payment flow:

```text
Order Created
      ↓
Payment PENDING
      ↓
Payment SUCCESS
      ↓
Order CONFIRMED
      ↓
Inventory Consumed
      ↓
Cart Cleared
```

### 🔔 Notifications

Notifications are generated for important order and payment events:

* Order confirmed
* Order processing
* Order shipped
* Order delivered
* Order cancelled
* Payment successful
* Payment failed
* Payment refunded

### ⭐ Reviews & Wishlist

* Product reviews
* 1–5 star ratings
* One review per user per product
* Wishlist management
* Wishlist items

### 👨‍💼 Admin

Admin functionality includes:

* Dashboard
* User management
* Product inventory overview
* Order management
* Order status updates
* Coupon management
* Revenue overview
* Pending order tracking

### 🛡️ Security

* Spring Security
* JWT authentication
* Role-based authorization
* Stateless authentication
* Protected admin endpoints
* Custom `401 Unauthorized` response
* Custom `403 Forbidden` response
* Resource-level order authorization

### ⚠️ Error Handling

* Global exception handling
* Custom resource-not-found exceptions
* Validation error handling
* Consistent API error responses

## 🛠️ Tech Stack

* **Java 24**
* **Spring Boot 4**
* **Spring Security**
* **Spring Data JPA**
* **Hibernate**
* **MySQL 8**
* **Maven**
* **Lombok**
* **ModelMapper**
* **JWT**
* **Docker**
* **Docker Compose**
* **Postman**
* **Git & GitHub**

## 🏗️ Architecture

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

Business logic is kept inside the service layer, while repositories handle database access.

## 📁 Package Structure

```text
src/main/java/com/shashank/ecommerce
│
├── admin
│   ├── controller
│   ├── dto
│   └── service
│
├── auth
│   ├── controller
│   ├── dto
│   └── service
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
├── coupon
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── inventory
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── notification
│   ├── entity
│   ├── repository
│   └── service
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
├── security
│   ├── config
│   ├── filter
│   ├── handler
│   └── service
│
└── wishlist
    ├── controller
    ├── dto
    ├── entity
    ├── repository
    └── service
```

## 🐳 Docker

The application can be run using Docker Compose.

The Docker setup contains:

```text
Docker Compose
│
├── Spring Boot Application
│   └── Java 24
│
└── MySQL 8
```

The Spring Boot application runs inside the container on port `8080`.

For local access, Docker maps it to:

```text
http://localhost:8081
```

MySQL is kept inside the Docker network and is accessed by the application using:

```text
mysql:3306
```

A persistent Docker volume is used for MySQL data.

### Run with Docker

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

View application logs:

```bash
docker logs ecommerce-app
```

Stop the application:

```bash
docker compose down
```

### Environment Variables

Sensitive configuration is stored in a local `.env` file.

Example:

```env
MYSQL_ROOT_PASSWORD=your_password
SPRING_DATASOURCE_PASSWORD=your_password
```

The `.env` file is excluded from Git using `.gitignore`.

## 🔄 Complete Checkout Flow

The main customer checkout flow is:

```text
Cart
  ↓
Apply Coupon
  ↓
Create Order
  ↓
Validate Inventory
  ↓
Reserve Stock
  ↓
Payment PENDING
  ↓
Payment SUCCESS
  ↓
Consume Inventory
  ↓
Clear Cart
  ↓
Order CONFIRMED
  ↓
PROCESSING
  ↓
SHIPPED
  ↓
DELIVERED
```

Cancellation and refund flows restore inventory where applicable.

## 💰 Money Handling

All monetary values use Java `BigDecimal` instead of floating-point types.

This avoids precision problems when handling:

* Product prices
* Cart totals
* Discounts
* Order totals
* Payments
* Refunds
* Revenue

## 🔐 Security Design

Authentication flow:

```text
User Login
    ↓
Spring Security
    ↓
AuthenticationManager
    ↓
JWT Generated
    ↓
Client
    ↓
Authorization Header
    ↓
JWT Filter
    ↓
SecurityContext
    ↓
Protected Endpoint
```

Admin APIs are protected using role-based authorization.

```text
CUSTOMER → Customer APIs
ADMIN    → Admin APIs
```

## 📌 Future Improvements

Planned improvements include:

* React frontend
* Production deployment
* AWS deployment
* Redis caching
* Kafka-based asynchronous events
* Automated testing
* Payment gateway integration
* Email/SMS notifications
* CI/CD pipeline

## 👨‍💻 Author

**Shashank Shekhar Pandey**

Java Backend Developer | Spring Boot | REST APIs | MySQL

GitHub: `Shashank907`

## 📄 License

This project is intended for learning, portfolio development, and demonstration of backend engineering concepts.
