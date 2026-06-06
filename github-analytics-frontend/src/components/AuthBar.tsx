import { useEffect, useState } from 'react';
import { getCurrentUser, type AuthUser } from '../services/authApi';

export function AuthBar() {
  const [auth, setAuth] = useState<AuthUser | null>(null);

  useEffect(() => {
    getCurrentUser().then(setAuth);
  }, []);

  if (auth === null) return null;

  if (!auth.authenticated) {
    return (
      <div className="flex justify-end px-4 py-3 border-b border-gray-100 dark:border-gray-800">
        <a
          href="/oauth2/authorization/github"
          className="text-sm px-4 py-1.5 rounded-lg bg-gray-900 dark:bg-white text-white dark:text-gray-900 hover:opacity-80 transition"
        >
          Sign in with GitHub
        </a>
      </div>
    );
  }

  return (
    <div
      className="flex items-center justify-end gap-3 px-4 py-3
                 border-b border-gray-100 dark:border-gray-800"
    >
      {auth.avatarUrl && (
        <img
          src={auth.avatarUrl}
          alt={auth.login}
          className="w-7 h-7 rounded-full"
        />
      )}

      <span className="text-sm text-gray-600 dark:text-gray-300">
        {auth.name ?? auth.login}
      </span>

      <a
        href="/logout"
        className="text-sm px-3 py-1.5 rounded-lg border border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800 transition"
      >
        Sign out
      </a>
    </div>
  );
}