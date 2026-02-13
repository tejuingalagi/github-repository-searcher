# GitHub Repository Searcher – Spring Boot

## Overview

This project is a **Spring Boot REST API application** that allows users to search GitHub repositories using the GitHub REST API, store the results in a MySQL database, and retrieve stored repositories using filter criteria.

The application demonstrates REST API design, database persistence, API integration, error handling, and unit testing.

---

## Tech Stack

* Java 8 Compatible
* Spring Boot 2.7
* Spring Data JPA (Hibernate)
* MySQL Database
* RESTTemplate
* JUnit 5 & Mockito
* Maven

---

## Features

### 1. Search GitHub Repositories

Fetch repositories from GitHub API and store/update them in database.

**Endpoint**

```
POST /api/github/search
```

**Request Body**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

**Behavior**

* Fetches repositories from GitHub API
* Saves new repositories
* Updates existing repositories (prevents duplicates)
* Handles API failures and rate limits

---

### 2. Retrieve Stored Repositories

```
GET /api/github/repositories
```

**Query Parameters**

| Parameter | Description                    |
| --------- | ------------------------------ |
| language  | Filter by programming language |
| minStars  | Minimum star count             |
| sort      | stars / forks / updated        |

**Example**

```
GET /api/github/repositories?language=Java&minStars=100&sort=stars
```

---

## Database Fields Stored

* GitHub Repository ID
* Name
* Description
* Owner
* Language
* Stars Count
* Forks Count
* Last Updated Date

If repository already exists → it is updated instead of duplicated.

---

## Error Handling

Handles:

* Invalid GitHub API response
* API rate limit exceeded
* Empty search results
* Invalid request inputs

---

## Running the Project

### 1. Clone Repository

```
git clone https://github.com/tejuingalagi/github-repository-searcher.git
cd github-repository-searcher
```

### 2. Configure MySQL

Create database:

```
github_db
```

Update `application.properties` if needed:

```
spring.datasource.url=jdbc:mysql://localhost:3306/github_db
spring.datasource.username=root
spring.datasource.password=tiger
```

### 3. Run Application

```
mvn spring-boot:run
```

Server starts at:

```
http://localhost:8080
```

---

## Testing

Run unit tests:

```
mvn test
```

Includes:

* Controller tests (MockMvc)
* Service tests (Mockito)

---

## API Testing

Test endpoints using Postman.

---

## Design Approach

* Controller → Handles HTTP requests
* Service → Business logic
* Repository → Database operations
* DTO → API communication models
* Exception Handler → Centralized error handling

---

## Author

Tejeshwini Ingalagi
