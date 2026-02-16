# GitHub Repository Searcher API

A Spring Boot REST API that fetches repositories from the GitHub REST API, stores them in PostgreSQL, and provides filtered retrieval using sorting and pagination.

This project demonstrates backend API development, external API integration, database persistence, validation, and structured error handling.

---

## Overview

The application:

* Fetches repositories from GitHub Search API
* Stores them in PostgreSQL database
* Updates existing records to avoid duplicates
* Allows querying stored repositories using filters and sorting

GitHub API used:
https://api.github.com/search/repositories

---

## Tech Stack

* Java 8
* Spring Boot 2.7
* Spring Data JPA (Hibernate)
* PostgreSQL
* RESTTemplate
* Maven
* JUnit 5 & Mockito

---

## Features

### 1. Fetch Repositories from GitHub

Search repositories using:

* Repository name
* Programming language
* Sort (stars / forks / updated)

Handles:

* Invalid requests
* GitHub API failures
* Rate limit exceeded responses
* Empty search results

---

### 2. Store Results in PostgreSQL

Stored fields:

* GitHub Repository ID (unique)
* Name
* Description
* Owner
* Language
* Stars
* Forks
* Last Updated

**Update Instead of Duplicate**
If a repository already exists, its details are updated instead of inserting a new record.

---

### 3. Retrieve Stored Repositories

Supports filtering:

* Language
* Minimum stars
* Sorting
* Pagination

All filtering is performed in database queries for efficiency.

---

## API Endpoints

### Search GitHub & Save

**POST /api/github/search**

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

If GitHub returns no repositories, API responds successfully with an empty list.

---

### Get Stored Repositories

**GET /api/github/repositories**

#### Query Parameters

| Parameter | Description             |
| --------- | ----------------------- |
| language  | Filter by language      |
| minStars  | Minimum star count      |
| sort      | stars / forks / updated |
| page      | Page number             |
| size      | Page size               |

Example:

```
GET /api/github/repositories?language=Java&minStars=100&sort=stars&page=0&size=10
```

---

## Error Handling

Example invalid sort:

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

Handles:

* Invalid parameters
* GitHub API failures
* Rate limit exceeded
* Empty results

---

## Architecture

The application follows a layered architecture:

### Controller Layer
Handles HTTP requests, input validation, and response formatting.

### Service Layer
Contains business logic, integrates with GitHub API, and processes repository data.

### Repository Layer
Performs database operations using Spring Data JPA.

### Global Exception Handler
Provides consistent and structured error responses.

---

Flow:
Client → Controller → Service → GitHub API → Database → Response


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
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run Application

```
mvn clean install
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

Tests include:

* Controller validation tests
* Successful repository search
* Duplicate repository update
* Filtering & sorting retrieval
* Error handling scenarios

---

## Author

Tejeshwini Ingalagi
