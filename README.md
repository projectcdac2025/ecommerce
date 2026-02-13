
# 🛒 E-Commerce Full Stack Application

A full-stack **E-Commerce Web Application** built using:

* ⚙️ Spring Boot (Backend REST API)
* 🔐 Spring Security + JWT Authentication
* 🗄️ MySQL Database
* ⚛️ React (Vite) Frontend
* 🔄 Axios for API communication

---

# 📌 Features

## 🔐 Authentication & Security

* User Registration
* User Login
* JWT-based Authentication
* Role-based Authorization (ADMIN / USER)
* Protected Routes (Frontend & Backend)
* BCrypt Password Encryption

## 📦 Product Management

* View Products
* Add Product (ADMIN only)
* Update Product
* Delete Product

## 🎨 Frontend

* React + Vite setup
* Axios API integration
* JWT token handling
* PrivateRoute protection
* Clean component structure

---

# 🏗️ Project Architecture

## Backend Architecture

```
Controller → Service → Repository → Database
```

ecommerce-backend/
 ├── controller/
 ├── service/
 ├── repository/
 ├── entity/
 ├── security/
 ├── config/
 └── EcommerceApplication.java

---

## Frontend Architecture

ecommerce-frontend/
 ├── src/
 │    ├── api/
 │    ├── pages/
 │    ├── components/
 │    ├── App.jsx
 │    └── main.jsx
 ├── package.json
 └── vite.config.js

---

# 🚀 Backend Setup

## ✅ Prerequisites

* Java 17+
* Maven
* MySQL

---

## 1️⃣ Configure Database

Update `application.properties`:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


# 🎨 Frontend Setup

## ✅ Prerequisites

* Node.js (v18+)

Check:

```bash
node -v
npm -v
```

---

## 1️⃣ Install Dependencies

```bash
cd ecommerce-frontend
npm install
```

---

## 2️⃣ Run Frontend

```bash
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

---

# 🔗 API Endpoints

## 🔐 Authentication

| Method | Endpoint             | Description       |
| ------ | -------------------- | ----------------- |
| POST   | `/api/auth/register` | Register new user |
| POST   | `/api/auth/login`    | Login and get JWT |

---

## 📦 Products

| Method | Endpoint             | Access       |
| ------ | -------------------- | ------------ |
| GET    | `/api/products`      | USER / ADMIN |
| POST   | `/api/products`      | ADMIN only   |
| PUT    | `/api/products/{id}` | ADMIN only   |
| DELETE | `/api/products/{id}` | ADMIN only   |

---

# 🔐 JWT Authentication Flow

1. User logs in
2. Backend returns JWT token
3. Token stored in localStorage
4. Axios attaches token in header:

```
Authorization: Bearer <token>
```

5. Backend validates token on every request

---

# 👤 Roles & Access Control

| Role       | Permissions     |
| ---------- | --------------- |
| ROLE_USER  | View products   |
| ROLE_ADMIN | Manage products |

---

# 🧪 Testing Instructions

1. Start backend
2. Start frontend
3. Register new user
4. Login
5. If ADMIN → Add product
6. If USER → View products only

---

# ⚠ Common Issues & Solutions

### ❌ CORS Error

Allow frontend origin in backend:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

---

### ❌ 401 Unauthorized

* Token missing
* Token expired
* Invalid credentials

---

### ❌ 403 Forbidden

* User does not have ADMIN role

---

# 🛠 Tech Stack

### Backend

* Spring Boot
* Spring Security
* JWT
* Hibernate / JPA
* MySQL

### Frontend

* React (Vite)
* Axios
* React Router

---

# 📈 Future Improvements

* Pagination & Sorting
* Caching(Redis)
* Product Search
* Cart & Order Module
* Refresh Token Mechanism
* Docker Deployment
* Cloud Deployment (AWS / Render)

---

# 👨‍💻 Author

Developed as a full-stack learning project demonstrating:

* Secure REST API development
* Role-based access control
* JWT authentication
* Frontend-backend integration
