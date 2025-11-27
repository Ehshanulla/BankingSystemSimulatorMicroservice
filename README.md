# Banking System Simulator -- Microservices Architecture

![Java](https://img.shields.io/badge/Java-21-blue) ![Spring
Boot](https://img.shields.io/badge/Spring%20Boot-Microservices-green)
![Spring
Cloud](https://img.shields.io/badge/Spring%20Cloud-Netflix%20Eureka-orange)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![Swagger](https://img.shields.io/badge/Swagger-Enabled-brightgreen)
![Status](https://img.shields.io/badge/Status-Active-success)

A fully containerized **Spring Boot Microservices Banking Simulator**
using: - Eureka Service Registry\
- API Gateway\
- Swagger UI\
- Docker Compose\
- Independent microservices for Account, Transactions, and Notifications

------------------------------------------------------------------------

# 📌 Table of Contents

-   [Overview](#overview)
-   [Architecture](#architecture)
-   [Project Structure](#project-structure)
-   [Features](#features)
-   [Tech Stack](#tech-stack)
-   [Setup](#setup)
-   [Running with Docker](#running-with-docker)
-   [Service URLs](#service-urls)
-   [Swagger Documentation](#swagger-documentation)
-   [API Endpoints](#api-endpoints)
-   [Postman Collection](#postman-collection)
-   [Future Enhancements](#future-enhancements)
-   [License](#license)

------------------------------------------------------------------------

# 🧩 Overview

This project simulates a modular banking backend using a **microservices
architecture**.\
It demonstrates real-world banking operations:

-   Create & manage accounts\
-   Perform deposits, withdrawals, transfers\
-   Generate notifications\
-   Dynamic service discovery\
-   Load-balanced API Gateway routing\
-   Auto-generated REST documentation using Swagger UI

------------------------------------------------------------------------

# 🏛 Architecture

``` mermaid
flowchart TD
    UI[Client / UI] --> APIGW[API Gateway]

    APIGW --> ACC[Account Service]
    APIGW --> TRN[Transaction Service]
    APIGW --> NOTIF[Notification Service]

    ACC --> EU[Eureka Server]
    TRN --> EU
    NOTIF --> EU
    APIGW --> EU
```

------------------------------------------------------------------------

# 📁 Project Structure

    BankingSystemSimulatorMicroservice/
    │
    ├── API-Gateway/
    ├── AccountMicroService/
    ├── TransactionsMicroService/
    ├── NotificationMicroService/
    ├── Eureka/
    └── docker-compose.yml

------------------------------------------------------------------------

# ⭐ Features

### ✔ Account Service

-   Create account\
-   Fetch account\
-   Update balance

### ✔ Transaction Service

-   Deposit\
-   Withdraw\
-   Transfer\
-   Generate transaction history

### ✔ Notification Service

-   Sends transaction notifications

### ✔ API Gateway

-   Central entry point\
-   Routes requests automatically using Eureka

### ✔ Eureka Server

-   Registers microservices dynamically\
-   Enables load balancing

### ✔ Swagger UI

Provides **visual API documentation** for each service at:

    http://localhost:<service-port>/swagger-ui/index.html

------------------------------------------------------------------------

# 🛠 Tech Stack

  Component          Technology
  ------------------ --------------------------------
  Language           Java 17
  Framework          Spring Boot
  Discovery          Spring Cloud Netflix Eureka
  API Docs           Swagger UI (Springdoc OpenAPI)
  Build Tool         Maven
  Containerization   Docker, Docker Compose
  Architecture       Microservices

------------------------------------------------------------------------

# 🚀 Setup

### 1️⃣ Clone the Repository

``` bash
git clone https://github.com/Ehshanulla/BankingSystemSimulatorMicroservice.git
cd BankingSystemSimulatorMicroservice
```

### 2️⃣ Build All Microservices

``` bash
mvn clean install
```

------------------------------------------------------------------------

# 🐳 Running with Docker

``` bash
docker-compose up --build
```

This launches: - API Gateway\
- Eureka Server\
- Account Service\
- Transactions Service\
- Notification Service

------------------------------------------------------------------------

# 🌐 Service URLs

  ----------------------------------------------------------------------------------------------------
  Service                                     URL
  ------------------------------------------- --------------------------------------------------------
  **API Gateway**                             http://localhost:8080

  **Eureka Dashboard**                        http://localhost:8761

  **Account Service Swagger**                 http://localhost:`<port>`{=html}/swagger-ui/index.html

  **Transactions Service Swagger**            http://localhost:`<port>`{=html}/swagger-ui/index.html

  **Notification Service Swagger**            http://localhost:`<port>`{=html}/swagger-ui/index.html
  ----------------------------------------------------------------------------------------------------

> Ports for microservices are dynamically assigned by Eureka unless
> specified manually.

# 📡 API Endpoints

## 1. Create Account

**POST** `/accounts/create`

``` json
{
  "name": "John Doe",
  "balance": 1000
}
```

## 2. Deposit

**POST** `/transactions/<accountNumber>/deposit/`

``` json
{
  "amount": 500
}
```

## 3. Withdraw

**POST** `/transactions/<accountNumber>/withdraw`

``` json
{
  "amount": 200
}
```

## 4. Transfer

**POST** `/transactions/transfer`

``` json
{
  "fromAccount": "123",
  "toAccount": "456",
  "amount": 250
}
```

## 5. Get Account Details

**GET** `/accounts/{id}`




# 🚧 Future Enhancements

-   Add **JWT Authentication**
-   User Management Service\
-   Loan/FD/RD Microservices\
-   Centralized Logging (ELK)\
-   API Rate Limiting\

------------------------------------------------------------------------

# 📄 License

This project is licensed under the **MIT License**.
