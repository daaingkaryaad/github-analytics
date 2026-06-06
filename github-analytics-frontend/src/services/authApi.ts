export interface AuthUser {
  authenticated: boolean;
  login?: string;
  name?: string;
  avatarUrl?: string;
}

export async function getCurrentUser(): Promise<AuthUser> {
  const res = await fetch('/api/me');
  if (!res.ok) return { authenticated: false };
  return res.json();
}