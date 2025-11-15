# **User Service — TechMarket Microservices**

The **User Service** is responsible for managing user accounts, authentication, security, and profile data in the **TechMarket** microservices ecosystem.
It exposes REST APIs and communicates with other services through secure HTTP calls.

---

## **Features**

* Customer registration
* add admin pre-creation on application startup
* Login & JWT authentication
* Refresh token system
* Role-based authorization
* Password hashing
* Update profile
* Find user by ID or email
* Integration with API Gateway
* PostgreSQL persistence
* Exception handling & validation

---

## **Tech Stack**

* **Java 21**
* **Spring Boot 3**
* **Spring Security 6**
* **Spring Data JPA**
* **PostgreSQL**
* **JWT Authentication**
* **Lombok**

---

## **Authentication Flow**

1. User signs in using email/password
2. Service generates:

    * **Access Token (JWT)**
    * **Refresh Token**
3. Access token is sent on every request
4. When it expires, client uses refresh token to get a new access token

---

## **API Endpoints**

### Auth Controller

| Method | Endpoint             | Description               |
| ------ |----------------------|---------------------------|
| POST   | `/api/user/register` | Create new customer       |
| POST   | `/api/user/login`    | Authenticate & get JWT    |
| POST   | `/api/user/refresh`  | Generate new access token |

---

## **Configuration**

  port: 8081

## **Refresh Token Logic**

* Stored in database
* Each user has one refresh token
* When expired → generate a new one
* Prevents stolen JWT problems
