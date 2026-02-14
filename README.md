# GitHub Repository Searcher API

## Overview

A Spring Boot REST API that fetches repositories from GitHub, stores them in PostgreSQL, and provides filtering, sorting and pagination.

This project demonstrates clean backend architecture, database filtering, upsert handling and production-style REST API design.

---

## Tech Stack

* Java 8 Compatible
* Spring Boot 2.7
* Spring Data JPA (Hibernate)
* PostgreSQL
* REST API
* Maven
* JUnit & Mockito

---

## Features

### 1. Fetch repositories from GitHub

Search GitHub repositories using:

* Repository name (partial/full match)
* Programming language
* Sort by stars / forks / updated

The application calls the GitHub API:

```
https://api.github.com/search/repositories
```

---

### 2. Store results in PostgreSQL

Stores repository details:

* Repository ID (unique from GitHub)
* Name
* Description
* Owner
* Language
* Stars
* Forks
* Last Updated Date

If repository already exists → it updates instead of duplicate (**UPSERT behavior**).

---

### 3. Retrieve stored repositories

Supports database-level filtering:

* Filter by language
* Filter by minimum stars
* Sorting
* Pagination

Filtering is executed at database level using JPA query for better performance.

---

## API Endpoints

### 1️⃣ Search GitHub and Save

**POST** `/api/github/search`

Request:

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

Response:

```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [...]
}
```

---

### 2️⃣ Get Stored Repositories

**GET** `/api/github/repositories`

Query Parameters:

| Parameter | Description                    |
| --------- | ------------------------------ |
| language  | Filter by programming language |
| minStars  | Minimum star count             |
| sort      | stars / forks / updated        |
| page      | Page number                    |
| size      | Page size                      |

Example:

```
GET /api/github/repositories?language=Java&minStars=100&sort=stars&page=0&size=5
```

---

## Pagination Supported

```
GET /api/github/repositories?page=0&size=10
```

---

## Error Handling

Invalid sort example:

```
GET /api/github/repositories?sort=random
```

Response:

```json
{
  "error": "Invalid Parameter",
  "message": "Invalid sort field. Use stars, forks or updated",
  "status": 400
}
```

---

## Architecture

Controller → Service → Repository → Database

Separation of concerns:

* Controller handles HTTP requests
* Service handles business logic
* Repository handles database queries
* Exception handler handles API errors

---

## How to Run

### 1. Clone project

```
git clone https://github.com/tejuingalagi/github-repository-searcher.git
cd github-repository-searcher
```

### 2. Configure PostgreSQL

Create database:

```
github_db
```

Update `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/github_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

---

### 3. Run application

```
mvn spring-boot:run
```

Server runs at:

```
http://localhost:8080
```

Test using Postman.

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

## Evaluation Criteria Covered

✔ REST compliant APIs
✔ PostgreSQL storage
✔ Upsert handling (no duplicates)
✔ Pagination
✔ Sorting validation
✔ Clean layered architecture
✔ Error handling
✔ Database filtering
✔ Testable via Postman

---

## Author

Tejeshwini Ingalagi
