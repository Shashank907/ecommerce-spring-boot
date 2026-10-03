# E-Commerce Backend

A production-oriented **e-commerce backend REST API** built with **Java 24 and Spring Boot 4**.

The application follows a layered architecture with separate controllers, services, repositories, DTOs, and entities. It implements the core customer-side e-commerce flow including **cart, coupons, inventory reservation, orders, payments, refunds, cancellation, and order lifecycle management**.

## 🚀 Features

* User management
* Address management
* Category management
* Product management
* Inventory management
* Shopping cart
* Cart items
* Coupon management
* Order management
* Order items
* Payment management
* Inventory reservation and stock consumption
* Order cancellation
* Payment refund
* Order status lifecycle
* Product reviews
* Wishlist
* Input validation
* Global exception handling
* JPA/Hibernate database integration
* `BigDecimal` for monetary values

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
├── coupon
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── exception
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

| Module    | Description                                          |
| --------- | ---------------------------------------------------- |
| User      | Customer and admin user management                   |
| Address   | User shipping addresses                              |
| Category  | Product categorization                               |
| Product   | Product creation and management                      |
| Inventory | Stock, reservation, consumption and restoration      |
| Cart      | Shopping cart and cart items                         |
| Coupon    | Coupon creation, validation and discount calculation |
| Order     | Order creation, cancellation and status lifecycle    |
| Payment   | Payment records, success, failure and refunds        |
| Review    | Product reviews and ratings                          |
| Wishlist  | Customer wishlist management                         |

## 🛒 Checkout Flow

The core customer checkout flow is:

```text
Add Product to Cart
        ↓
Apply Coupon (Optional)
        ↓
Create Order
        ↓
Reserve Inventory
        ↓
Create Payment
        ↓
Payment SUCCESS
        ↓
Consume Reserved Stock
        ↓
Order CONFIRMED
        ↓
PROCESSING
        ↓
SHIPPED
        ↓
DELIVERED
```

The application also supports cancellation flows.

### Pending Order Cancellation

```text
Order PENDING
      ↓
Cancel Order
      ↓
Release Reserved Stock
      ↓
Order CANCELLED
```

### Successful Payment Cancellation

```text
Payment SUCCESS
      ↓
Cancel Order
      ↓
Refund Payment
      ↓
Restore Inventory
      ↓
Order CANCELLED
```

## 📦 Inventory Management

Inventory maintains:

```text
quantity
reservedQuantity
availableQuantity
```

Where:

```text
availableQuantity = quantity - reservedQuantity
```

During order creation, stock is reserved.

After successful payment, reserved stock is consumed.

When an eligible order is cancelled, reserved stock or consumed stock is restored depending on the order/payment state.

## 💳 Payment Flow

Payments currently support the following states:

```text
PENDING
SUCCESS
FAILED
REFUNDED
```

Successful payment:

```text
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

## 📋 Order Lifecycle

Orders support:

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

Orders can also become:

```text
CANCELLED
```

Invalid status transitions are rejected by the service layer.

## 🏷️ Coupon System

The coupon module supports:

* Coupon creation
* Coupon updates
* Coupon validation
* Coupon application
* Percentage discounts
* Fixed-amount discounts
* Minimum order requirements
* Coupon usage tracking
* Expiration validation

Coupons can be applied while creating an order.

## 🗄️ Database

The application uses **MySQL**.

Database credentials are configured through environment variables.

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Set these environment variables before running the application:

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

### Products

```http
GET    /api/products
POST   /api/products
GET    /api/products/{id}
PUT    /api/products/{id}
DELETE /api/products/{id}
```

### Cart

```http
POST   /api/users/{userId}/cart
GET    /api/users/{userId}/cart
POST   /api/users/{userId}/cart/items
GET    /api/users/{userId}/cart/items
DELETE /api/users/{userId}/cart/items/{itemId}
```

### Orders

```http
POST /api/users/{userId}/orders
GET  /api/users/{userId}/orders
GET  /api/users/{userId}/orders/{orderId}
PUT  /api/users/{userId}/orders/{orderId}/cancel
PUT  /api/users/{userId}/orders/{orderId}/status
```

### Payments

```http
POST /api/orders/{orderId}/payment
GET  /api/orders/{orderId}/payment
POST /api/orders/{orderId}/payment/success
POST /api/orders/{orderId}/payment/failed
POST /api/orders/{orderId}/payment/refund
```

### Wishlist

```http
POST   /api/users/{userId}/wishlist
GET    /api/users/{userId}/wishlist
POST   /api/users/{userId}/wishlist/items/{productId}
GET    /api/users/{userId}/wishlist/items
DELETE /api/users/{userId}/wishlist/items/{productId}
```

### Reviews

```http
POST   /api/users/{userId}/products/{productId}/reviews
GET    /api/users/{userId}/products/{productId}/reviews
DELETE /api/users/{userId}/products/{productId}/reviews/{reviewId}
```

## 🧪 API Testing

The APIs are tested using **Postman**.

A typical checkout flow is:

```text
Create Category
      ↓
Create Product
      ↓
Create User
      ↓
Create Address
      ↓
Create Cart
      ↓
Add Product to Cart
      ↓
Apply Coupon (Optional)
      ↓
Create Order
      ↓
Reserve Inventory
      ↓
Create Payment
      ↓
Payment SUCCESS
      ↓
Order CONFIRMED
      ↓
PROCESSING
      ↓
SHIPPED
      ↓
DELIVERED
```

The core happy-path checkout flow and order lifecycle have been tested end-to-end.

## 🔒 Production Roadmap

The project is being developed toward a production-ready e-commerce backend.

Planned improvements include:

* JWT authentication
* BCrypt password hashing
* Role-based authorization
* Secure user-specific API access
* Product search
* Product filtering
* Pagination
* Sorting
* Improved exception handling
* Additional validation
* Unit testing
* Integration testing
* Docker containerization
* Redis caching
* Kafka/event-driven features
* React frontend
* API documentation
* Cloud deployment

## 📌 Project Status

| Component          | Status      |
| ------------------ | ----------- |
| User Management    | ✅ Completed |
| Address Management | ✅ Completed |
| Category           | ✅ Completed |
| Product            | ✅ Completed |
| Cart               | ✅ Completed |
| Coupon             | ✅ Completed |
| Inventory          | ✅ Completed |
| Orders             | ✅ Completed |
| Payments           | ✅ Completed |
| Order Lifecycle    | ✅ Completed |
| Wishlist           | ✅ Completed |
| Reviews            | ✅ Completed |
| Authentication     | 🚧 Planned  |
| Search/Filter/Sort | 🚧 Planned  |
| Docker             | 🚧 Planned  |
| Redis/Kafka        | 🚧 Planned  |
| React Frontend     | 🚧 Planned  |
| Deployment         | 🚧 Planned  |

## 👨‍💻 Author

**Shashank Shekhar Pandey**

B.Tech Computer Science & Engineering

GitHub:
https://github.com/Shashank907

## 📄 License

This project is currently intended for learning, portfolio development, and demonstration purposes.
