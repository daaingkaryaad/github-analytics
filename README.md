# GitHub Analytics

A full-stack web application for analyzing GitHub user profiles, repository statistics, and language distribution. Built as a portfolio project demonstrating a layered Spring Boot backend and a React frontend.

---

## Overview

GitHub Analytics accepts a GitHub username and returns a structured dashboard showing profile information, repository data, language breakdown, and aggregate statistics. All GitHub API communication is handled server-side, keeping rate limit tokens off the client and establishing a clean separation between data retrieval and presentation.

---

## Architecture

```
React (TypeScript)
    └── githubApi.ts (fetch wrapper)
            └── Spring Boot REST API
                    ├── GithubController  
                    ├── GithubService      
                    ├── GithubApiClient    
                    └── DTOs               
```

The frontend never calls the GitHub API directly. Every request goes through the Spring Boot backend, which maps raw GitHub responses to DTOs before returning them to the client.

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
- Spring WebFlux (WebClient for non-blocking HTTP)
- Lombok

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
│   │   │   └── LanguageChart.tsx
│   │   ├── pages/
│   │   │   └── Dashboard.tsx
│   │   ├── services/
│   │   │   └── githubApi.ts
│   │   ├── types/
│   │   │   └── github.ts
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── index.html
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   └── tsconfig.json
│
└── github-analytics-backend/
    └── src/main/java/com/githubanalytics/
        ├── controller/
        │   └── GithubController.java
        ├── service/
        │   └── GithubService.java
        ├── client/
        │   └── GithubApiClient.java
        ├── dto/
        │   ├── UserDTO.java
        │   └── RepoDTO.java
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

**Example response — `/api/users/torvalds`**

```json
{
  "login": "torvalds",
  "name": "Linus Torvalds",
  "bio": "Just a random Linux kernel developer",
  "avatar_url": "https://avatars.githubusercontent.com/u/1024025",
  "followers": 230000,
  "following": 0,
  "public_repos": 8,
  "created_at": "2011-09-03T15:26:22Z"
}
```

---

## Prerequisites

- Java 21
- Node.js 26 
- npm

A GitHub personal access token is optional but recommended. Without one, the GitHub API applies a rate limit of 60 requests per hour per IP. With a token, the limit increases to 5000 requests per hour.

---

## Getting Started

**1. Clone the repository**

```bash
git clone https://github.com/daaingkaryaad/github-analytics.git
cd github-analytics
```

**2. Start the backend**

```bash
cd github-analytics-backend
./mvnw spring-boot:run
```

To use a GitHub token, pass it as an environment variable:

```bash
GITHUB_TOKEN=your_token_here ./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`.

**3. Start the frontend**

In a separate terminal:

```bash
cd github-analytics-frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`.

**4. Open the application**

Navigate to `http://localhost:5173`, enter any GitHub username, and the dashboard will populate.

---

## Features

- Profile overview: avatar, bio, follower and repository counts
- Repository list sorted by stars, excluding forks, capped at top 10
- Language distribution chart across all original repositories
- Aggregate statistics: total stars, total repositories, unique language count
- Error handling for unknown usernames and GitHub API failures

---

## Roadmap

The following features are planned for subsequent versions.

**Version 2**
- GitHub OAuth login
- PostgreSQL persistence for user profiles and repository data
- Redis caching to reduce GitHub API calls and improve response times
- AI-generated profile insights

**Version 3**
- Team and organization analytics
- Contribution heatmaps
- Comparative analysis across multiple users
- Dockerized deployment to Railway or Render

---

## Notes

This project was built as a portfolio piece to demonstrate layered backend architecture, typed API contracts between services, and component-based frontend design. The codebase intentionally avoids over-engineering for the MVP scope while maintaining a structure that supports the planned Version 2 features without significant refactoring.