# GitHub Analytics

A full-stack web application for analyzing GitHub user profiles, repository statistics, and language distribution. Built as a portfolio project demonstrating a layered Spring Boot backend, a React frontend, GitHub OAuth authentication, PostgreSQL persistence, Redis caching, and containerized deployment on Railway.

---

## Live Demo

Frontend: https://calm-nature-production-726c.up.railway.app

---

## Overview

GitHub Analytics accepts a GitHub username and returns a structured dashboard showing profile information, repository data, language breakdown, and aggregate statistics. All GitHub API communication is handled server-side, keeping rate limit tokens off the client and establishing a clean separation between data retrieval and presentation.

Authentication is optional. The search functionality works without logging in. Signing in with GitHub unlocks the authenticated session and identifies the current user via `/api/me`.

---

## Architecture

```
React (TypeScript)
    └── githubApi.ts / authApi.ts (fetch wrapper)
            └── Nginx (reverse proxy)
                    └── Spring Boot REST API
                            ├── GithubController    — search and stats endpoints
                            ├── AuthController      — /api/me current user
                            ├── GithubService       — business logic, aggregation
                            ├── GithubApiClient     — outbound calls to GitHub API
                            ├── DTOs                — typed response objects
                            ├── Spring Security     — OAuth2 login via GitHub
                            ├── PostgreSQL          — user and repository persistence
                            └── Redis               — API response caching
```

The frontend never calls the GitHub API directly. Every request goes through the Spring Boot backend, which maps raw GitHub responses to DTOs before returning them to the client. In production, Nginx proxies all `/api`, `/oauth2`, `/login`, and `/logout` traffic to the backend, keeping everything on a single origin.

---

## Tech Stack

**Frontend**
- React 18 with TypeScript
- Vite
- Tailwind CSS
- Recharts

**Backend**
- Java 21
- Spring Boot 3.5
- Spring Security with OAuth2 Client
- Spring WebFlux (WebClient for non-blocking HTTP)
- Spring Data JPA
- Lombok

**Infrastructure**
- PostgreSQL
- Redis
- Docker (multi-stage builds)
- Railway (deployment)
- Nginx (frontend reverse proxy)
- GitHub Actions (CI/CD)

---

## Project Structure

```
github-analytics/
├── github-analytics-frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── SearchBar.tsx
│   │   │   ├── UserCard.tsx
│   │   │   ├── RepoList.tsx
│   │   │   ├── LanguageChart.tsx
│   │   │   └── AuthBar.tsx
│   │   ├── pages/
│   │   │   └── Dashboard.tsx
│   │   ├── services/
│   │   │   ├── githubApi.ts
│   │   │   └── authApi.ts
│   │   ├── types/
│   │   │   └── github.ts
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── index.html
│   ├── nginx.conf
│   ├── Dockerfile
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   └── tsconfig.json
│
└── github-analytics-backend/
    └── src/main/java/com/githubanalytics/
        ├── config/
        │   └── SecurityConfig.java
        ├── controller/
        │   ├── GithubController.java
        │   └── AuthController.java
        ├── service/
        │   └── GithubService.java
        ├── client/
        │   └── GithubApiClient.java
        ├── dto/
        │   ├── UserDTO.java
        │   ├── RepoDTO.java
        │   └── CommitDTO.java
        ├── entity/
        │   ├── GithubUser.java
        │   └── GithubRepo.java
        ├── repository/
        │   ├── UserRepository.java
        │   └── RepoRepository.java
        ├── exception/
        │   ├── GlobalExceptionHandler.java
        │   └── GithubUserNotFoundException.java
        └── GithubAnalyticsApplication.java
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/{username}` | Returns profile information for a GitHub user |
| GET | `/api/users/{username}/repos` | Returns all public repositories |
| GET | `/api/users/{username}/stats` | Returns aggregate stats: total stars, language map, top repository |
| GET | `/api/me` | Returns current authenticated user or `{ "authenticated": false }` |

**Example response — `/api/users/torvalds`**

```json
{
  "login": "torvalds",
  "name": "Linus Torvalds",
  "bio": null,
  "avatar_url": "https://avatars.githubusercontent.com/u/1024025?v=4",
  "html_url": "https://github.com/torvalds",
  "followers": 305968,
  "following": 0,
  "public_repos": 12,
  "created_at": "2011-09-03T15:26:22Z"
}
```

---

## Prerequisites

- Java 21
- Node.js 20 or higher
- npm
- Docker (for local full-stack development)

A GitHub personal access token is optional but recommended. Without one, the GitHub API applies a rate limit of 60 requests per hour per IP. With a token, the limit increases to 5000 requests per hour.

---

## Getting Started

**1. Clone the repository**

```bash
git clone https://github.com/daaingkaryaad/github-analytics.git
cd github-analytics
```

**2. Start PostgreSQL and Redis via Docker**

```bash
docker run -d --name postgres \
  -e POSTGRES_DB=analytics \
  -e POSTGRES_USER=analytics \
  -e POSTGRES_PASSWORD=analytics \
  -p 5432:5432 postgres:16

docker run -d --name redis -p 6379:6379 redis:7
```

**3. Start the backend**

```bash
cd github-analytics-backend
GITHUB_TOKEN=your_token_here ./mvnw spring-boot:run
```

To enable GitHub OAuth login locally, also pass:

```bash
GITHUB_CLIENT_ID=your_id \
GITHUB_CLIENT_SECRET=your_secret \
./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`.

**4. Start the frontend**

In a separate terminal:

```bash
cd github-analytics-frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`.

---

## Environment Variables

**Backend**

| Variable | Description | Default |
|----------|-------------|---------|
| `GITHUB_TOKEN` | GitHub personal access token for higher rate limits | empty |
| `GITHUB_CLIENT_ID` | GitHub OAuth App client ID | empty |
| `GITHUB_CLIENT_SECRET` | GitHub OAuth App client secret | empty |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/analytics` |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL username | `analytics` |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password | `analytics` |
| `SPRING_DATA_REDIS_HOST` | Redis host | `localhost` |
| `SPRING_DATA_REDIS_PORT` | Redis port | `6379` |
| `FRONTEND_URL` | Frontend URL for OAuth redirects | `http://localhost:5173` |

**Frontend**

No runtime environment variables required. The Nginx config proxies all backend traffic at container startup.

---

## Features

- Profile overview: avatar, bio, follower and repository counts
- Repository list sorted by stars, excluding forks, capped at top 10
- Language distribution chart across all original repositories
- Aggregate statistics: total stars, total repositories, unique language count
- GitHub OAuth login with session persistence
- Current user display with sign out
- Error handling for unknown usernames and GitHub API failures
- Redis caching to reduce GitHub API calls
- PostgreSQL persistence for user and repository data
- Dockerized frontend and backend with multi-stage builds
- CI/CD via GitHub Actions
- Deployed on Railway

---

## Roadmap

**Version 3**
- Team and organization analytics
- Contribution heatmaps
- Comparative analysis across multiple users
- AI-generated profile insights

---

## Notes

This project was built as a portfolio piece to demonstrate layered backend architecture, typed API contracts between services, OAuth2 authentication, infrastructure setup, and containerized deployment. The codebase follows a Controller → Service → Client → DTO pattern throughout, with Spring Security handling the authentication layer non-invasively — unauthenticated users retain full access to all search functionality.