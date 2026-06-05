import type { GitHubUser, GitHubRepo, UserStats } from '../types/github';

const BASE = 'http://localhost:8080/api';

async function get<T>(path: string): Promise<T> {
  const res = await fetch(`${BASE}${path}`);
  if (!res.ok) {
    const err = await res.json().catch(() => ({ error: 'Unknown error' }));
    throw new Error(err.error ?? `Request failed: ${res.status}`);
  }
  return res.json();
}

export const githubApi = {
  getUser: (username: string) =>
    get<GitHubUser>(`/users/${username}`),

  getRepos: (username: string) =>
    get<GitHubRepo[]>(`/users/${username}/repos`),

  getStats: (username: string) =>
    get<UserStats>(`/users/${username}/stats`),
};