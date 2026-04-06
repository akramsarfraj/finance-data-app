# Finance Data Application

A Spring Boot-based backend application to manage financial transactions, users, and analytics with role-based access control.

---

## Tech Stack

* Java 17+
* Spring Boot
* PostgreSQL
* Spring Security (Authentication & Authorization)
* REST APIs

---

## Setup & Run Instructions

### Clone the Repository

```bash
git clone <your-repo-url>
cd finance-data-app
```

###  Configure Database (PostgreSQL)

* Create a PostgreSQL database:

```sql
CREATE DATABASE finance_db;
```

* Update `application.properties` or `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### Run the Application

Main class:

```
FinancedataApplication
```

Run using:

```bash
mvn spring-boot:run
```

OR run directly from your IDE.

---

## Authentication Flow

### Step 1: Create Admin

Before anything, create an admin user:

```
POST /create-admin
```

---

### Step 2: Login

Authenticate using:

```
POST /login
```

**Request Body:**

```json
{
  "email": "admin@example.com",
  "password": "password"
}
```

---

## User Roles

| Role    | ID | Description                                     |
| ------- | -- | ----------------------------------------------  |
| ADMIN   | 1  | Full access                                     |
| ANALYST | 2  | Read & analytics(dashboard and transaction api) |
| USER    | 3  | Limited access (dashboard)                      |

---

## User Management APIs

###  Create User (Admin Only)

Admin can create users with roles: ANALYST, USER

---

### Get All Users

```
GET /users/all
```

---

### Soft Delete User

```
DELETE /user
```

---

### Recover Deleted User

```
PUT /user/recover
```

---

### Permanent Delete User (Admin Only)

```
DELETE /user/permanent
```

---

##  Transaction APIs

### ➕ Create Transaction (Admin Only)

```
POST /create-record
```

---

###  Update Transaction

```
PUT /update-record
```

---

### Delete Transaction

```
DELETE /delete-record
```

---

###  Get All Transactions

```
GET /get-all-record
```

Accessible by:

* Admin
* Analyst

---

## Notes

* Ensure PostgreSQL is running before starting the app.
* Admin must be created before accessing secured APIs.
* Soft delete keeps user data recoverable.
* Role-based access control is enforced across APIs.

---

## Enhancements added

* JWT Authentication
* Swagger API Documentation (http://localhost:8080/swagger-ui/index.html#/)
* Dashboard & Analytics APIs
* Pagination & Filtering

⭐ If you like this project, consider giving it a star!
