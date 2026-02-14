# GitHub Repository Searcher API

A production-style Spring Boot REST API that searches repositories from GitHub, stores them in PostgreSQL, and provides advanced filtering, sorting and pagination.

This project demonstrates clean backend architecture, external API integration, database upsert handling and robust error handling.

---

## Overview

The application:

1. Fetches repositories from GitHub Search API
2. Stores them in PostgreSQL
3. Updates existing records (prevents duplicates)
4. Allows querying stored repositories using filters, sorting and pagination

GitHub API used:

```
https://api.github.com/search/repositories
```

---

## Tech Stack

- Java 8
- Spring Boot 2.7
- Spring Data JPA (Hibernate)
- PostgreSQL
- RESTTemplate (External API Integration)
- Maven
- JUnit & Mockito

---

## Features

### 1. Fetch Repositories from GitHub

Search repositories using:

- Repository name
- Programming language
- Sort (stars / forks / updated)

Handles:

- URL encoding of queries
- GitHub API failures
- Rate limit errors

---

### 2. Store Results in PostgreSQL

Stored fields:

- GitHub Repository ID (Unique)
- Name
- Description
- Owner
- Language
- Stars
- Forks
- Last Updated

**UPSERT Behavior**

If repository already exists → record is updated instead of duplicated  
(implemented using unique constraint on `github_repo_id`)

---

### 3. Retrieve Stored Repositories

Supports database-level filtering:

- Filter by language
- Filter by minimum stars
- Sorting
- Pagination

All filtering happens in database (JPA query) for performance.

---

## API Endpoints

### Search GitHub & Save

**POST** `/api/github/search`

#### Request
```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

#### Response
```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [...]
}
```

---

### Get Stored Repositories

**GET** `/api/github/repositories`

#### Query Parameters

| Parameter | Description |
|--------|------|
| language | Filter by language |
| minStars | Minimum star count |
| sort | stars / forks / updated |
| page | Page number |
| size | Page size |

#### Example
```
GET /api/github/repositories?language=Java&minStars=100&sort=stars&page=0&size=5
```

---

## Pagination Example

```
GET /api/github/repositories?page=0&size=10
```

---

## Error Handling

Invalid sort example:

```
GET /api/github/repositories?sort=random
```

#### Response
```json
{
  "error": "Invalid Parameter",
  "message": "Invalid sort field. Use stars, forks or updated",
  "status": 400
}
```

Handles:

- Invalid parameters
- GitHub API failures
- Rate limit exceeded
- Empty responses

---

## Architecture

```
Controller → Service → Repository → Database
```

### Responsibilities

- Controller → Handles HTTP requests
- Service → Business logic & GitHub integration
- Repository → Database queries
- Global Exception Handler → Centralized error responses

---

## How to Run

### 1. Clone Project
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

### 3. Run Application
```
mvn spring-boot:run
```

Server runs at:
```
http://localhost:8080
```

Test using Postman or Swagger.

---

## Testing

Run unit tests:

```
mvn test
```

Includes:

- Controller tests (MockMvc)
- Service tests (Mockito)

---

## Key Backend Concepts Demonstrated

- RESTful API design
- External API integration
- Database upsert handling
- Pagination & sorting
- Dynamic filtering
- Global exception handling
- Clean layered architecture

---

## Author

**Tejeshwini Ingalagi**
