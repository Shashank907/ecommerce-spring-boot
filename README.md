# 🛒 E-Commerce Backend

A production-oriented **E-Commerce Backend REST API** built using **Java 24, Spring Boot 4, Spring Security, JPA/Hibernate, and MySQL**.

The project focuses on real-world backend engineering concepts such as **JWT authentication, role-based authorization, inventory reservation, concurrency control, payment processing, order lifecycle management, coupons, notifications, reviews, wishlist, and Docker-based deployment**.

---

## 🚀 Features

### 👤 User & Authentication

* User registration and login
* JWT-based authentication
* BCrypt password hashing
* Role-based authorization
* Customer and Admin roles
* User account status management
* Custom `401 Unauthorized` response
* Custom `403 Forbidden` response
* Resource-level authorization
* Protected APIs using Spring Security

### 📦 Product Management

* Create products
* Update products
* Delete products
* Get product by ID
* Get all products
* Product search
* Product filtering
* Sorting
* Pagination
* Category-based product management

### 🗂️ Category Management

* Create category
* Update category
* Delete category
* Get category by ID
* Get all categories

### 🛒 Cart

* Add product to cart
* Update cart item quantity
* Remove cart item
* Get user's cart
* Clear cart
* Automatic cart total calculation

### 🎟️ Coupon

* Create coupons
* Update coupons
* Deactivate coupons
* Get coupons
* Percentage-based discounts
* Fixed-amount discounts
* Minimum order validation
* Coupon expiration
* Coupon usage limits
* Coupon usage tracking
* Admin coupon management

### 📦 Inventory

* Product inventory management
* Stock availability checking
* Stock reservation
* Stock consumption
* Stock release
* Stock restoration
* Available stock calculation
* Inventory protection using pessimistic locking
* Concurrency-safe stock reservation

Inventory follows:

```text
Available Stock = Total Quantity - Reserved Quantity
```

### 📋 Orders

* Create orders from cart
* Order item management
* Order history
* Get order by ID
* Customer order cancellation
* Admin order management
* Order status lifecycle
* Resource-level order authorization

Order lifecycle:

```text
CONFIRMED
    ↓
PROCESSING
    ↓
SHIPPED
    ↓
DELIVERED
```

Orders can also be cancelled when applicable.

### 💳 Payment

* Create payment
* Payment status management
* Payment success handling
* Payment failure handling
* Payment refund
* Payment idempotency
* Transaction ID
* Payment timestamp tracking
* BigDecimal-based monetary calculations

Payment states:

```text
PENDING
SUCCESS
FAILED
REFUNDED
```

### 🔔 Notifications

The system generates notifications for important order and payment events.

Supported notification types include:

```text
ORDER_CONFIRMED
ORDER_PROCESSING
ORDER_SHIPPED
ORDER_DELIVERED
ORDER_CANCELLED

PAYMENT_SUCCESS
PAYMENT_FAILED
PAYMENT_REFUNDED
```

Users can retrieve their notifications ordered by creation time.

### ⭐ Reviews & Ratings

* Product reviews
* Product ratings
* Rating validation from 1–5
* Review comments
* One review per user per product
* Review timestamps

### ❤️ Wishlist

* Add product to wishlist
* Remove product from wishlist
* View wishlist
* Prevent duplicate wishlist items

### 👑 Admin

Admin APIs provide management capabilities for:

* Dashboard
* Users
* Products
* Inventory
* Orders
* Coupons

Admin dashboard provides information such as:

```text
Total Users
Total Products
Total Orders
Pending Orders
Total Revenue
```

Admin APIs are protected using:

```text
ROLE_ADMIN
```

---

# 📚 API Documentation

The project uses **Swagger / OpenAPI** for interactive API documentation and API testing.

Swagger automatically discovers REST endpoints exposed by the application.

## Swagger UI

When running locally:

```text
http://localhost:8081/api/swagger-ui.html
```

Alternative:

```text
http://localhost:8081/api/swagger-ui/index.html
```

## OpenAPI Specification

The generated OpenAPI specification is available at:

```text
http://localhost:8081/api/v3/api-docs
```

## 🔐 JWT Authentication in Swagger

Swagger is configured with Bearer JWT authentication.

To test protected APIs:

1. Login using the authentication API.
2. Copy the generated JWT token.
3. Click **Authorize 🔒** in Swagger.
4. Enter:

```text
Bearer <your-jwt-token>
```

5. Click **Authorize**.

Swagger will then automatically send the JWT token with protected API requests.

## API Base Path

All application APIs use the common:

```text
/api
```

context path.

Examples:

```text
GET    /api/products
GET    /api/products/{id}
POST   /api/products

GET    /api/cart
POST   /api/cart

GET    /api/users/{userId}/orders
POST   /api/users/{userId}/orders

GET    /api/admin/orders
GET    /api/admin/users
GET    /api/admin/inventory
```

---

# ⚠️ Error Handling

The application provides centralized error handling through a global exception handler.

Features include:

* Global exception handling
* Resource-not-found exceptions
* Validation error handling
* Authentication error handling
* Authorization error handling
* Consistent API error responses
* Custom `401 Unauthorized` responses
* Custom `403 Forbidden` responses
* Conflict handling for invalid business operations

Example:

```json
{
  "status": 401,
  "message": "Authentication required"
}
```

---

# 🛠️ Tech Stack

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
* **Swagger / OpenAPI**
* **Docker**
* **Docker Compose**
* **Postman**
* **Git & GitHub**

---

# 🏗️ Architecture

The application follows a layered architecture:

```text
                    REST API
                       │
                       ↓
                 Controller Layer
                       │
                       ↓
                  Service Layer
                       │
                       ↓
                Repository Layer
                       │
                       ↓
                    MySQL
```

DTOs are used to transfer data between the API and client instead of exposing entities directly.

Business logic is maintained inside the service layer, while repositories are responsible for database access.

Security is handled using Spring Security and JWT authentication.

---

# 📁 Package Structure

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
├── wishlist
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
└── config
    └── OpenApiConfig.java
```

---

# 🐳 Docker

The application is fully containerized using **Docker and Docker Compose**.

The Docker environment contains:

```text
Docker Compose
│
├── Spring Boot Application
│   └── Java 24
│
└── MySQL 8
```

The Spring Boot application runs inside the container on:

```text
8080
```

Docker maps it to:

```text
8081
```

Therefore the application is accessible locally at:

```text
http://localhost:8081
```

The API base URL is:

```text
http://localhost:8081/api
```

MySQL is not exposed directly to the host machine.

The Spring Boot application communicates with MySQL through the Docker network:

```text
mysql:3306
```

A persistent Docker volume is used to preserve MySQL data.

---

# ▶️ Run with Docker

Start the application:

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

View live logs:

```bash
docker logs -f ecommerce-app
```

Stop the application:

```bash
docker compose down
```

---

# 🔐 Environment Variables

Sensitive configuration is stored using environment variables.

Example `.env`:

```env
MYSQL_ROOT_PASSWORD=your_password
SPRING_DATASOURCE_PASSWORD=your_password
```

The `.env` file is excluded from Git using `.gitignore`.

Never commit real database passwords, JWT secrets, API keys, or other credentials to the repository.

---

# 🔄 Complete Checkout Flow

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

### Payment Failure

```text
Cart
  ↓
Create Order
  ↓
Reserve Stock
  ↓
Payment PENDING
  ↓
Payment FAILED
  ↓
Release Inventory
  ↓
Order CANCELLED
  ↓
Notification Created
```

### Order Cancellation

Where cancellation is allowed:

```text
Order
  ↓
CANCELLED
  ↓
Release Reserved Inventory
  ↓
Notification Created
```

### Refund

```text
Payment SUCCESS
      ↓
Refund
      ↓
Payment REFUNDED
      ↓
Inventory Restored
      ↓
Order CANCELLED
      ↓
Notification Created
```

---

# 🔒 Security Design

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
JWT Authentication Filter
    ↓
JWT Validation
    ↓
SecurityContext
    ↓
Protected Endpoint
```

The application uses stateless JWT authentication.

Passwords are securely stored using BCrypt.

Role-based authorization:

```text
CUSTOMER
   ↓
Customer APIs

ADMIN
   ↓
Admin APIs
```

Admin endpoints are protected using:

```text
ROLE_ADMIN
```

---

# 🔐 Authorization

The application supports:

### Authentication

```text
JWT Bearer Token
```

### Roles

```text
CUSTOMER
ADMIN
```

### Access Control

```text
Public
  ↓
Authentication APIs

Authenticated User
  ↓
Customer APIs

ADMIN
  ↓
Administrative APIs
```

Resource-level authorization is also implemented to prevent users from accessing resources belonging to other users.

---

# ⚡ Inventory Concurrency Control

Inventory reservation uses database-level pessimistic locking to prevent race conditions when multiple requests attempt to reserve the same product simultaneously.

Conceptually:

```text
Request 1 ─────┐
               ↓
          Database Lock
               ↓
        Check Availability
               ↓
          Reserve Stock
               ↓
          Release Lock
               
Request 2 ─────┐
               ↓
          Wait for Lock
```

This helps prevent overselling when concurrent checkout requests target the same product.

---

# 💰 Money Handling

All monetary values use Java:

```text
BigDecimal
```

instead of floating-point types.

This prevents precision problems when handling:

* Product prices
* Cart totals
* Discounts
* Coupon amounts
* Order totals
* Payments
* Refunds
* Revenue

---

# 🔁 Payment Idempotency

The payment flow prevents an already successful payment from being processed again.

Conceptually:

```text
Payment PENDING
      ↓
Payment SUCCESS
      ↓
Repeated success request
      ↓
Return existing SUCCESS payment
```

This prevents duplicate payment processing and duplicate order-side effects.

---

# 📊 Database Design

The application uses **MySQL 8** with Spring Data JPA and Hibernate.

Major entities include:

```text
User
Address
Category
Product
Inventory
Cart
CartItem
Coupon
Order
OrderItem
Payment
Review
Wishlist
WishlistItem
Notification
```

Relationships are managed using JPA entity mappings and database constraints.

---

# 🧪 API Testing

APIs can be tested using:

* Swagger UI
* Postman

Swagger is recommended for interactive API documentation and authenticated API testing.

Postman can be used for:

* Authentication
* Checkout testing
* Payment flow testing
* Admin APIs
* Error scenarios
* Authorization testing

---

# 📌 Future Improvements

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
* Monitoring and logging improvements

---

# 👨‍💻 Author

**Shashank Shekhar Pandey**

Java Backend Developer | Spring Boot | REST APIs | MySQL

GitHub: `Shashank907`

---

# 📄 License

This project is intended for learning, portfolio development, and demonstration of backend engineering concepts.

