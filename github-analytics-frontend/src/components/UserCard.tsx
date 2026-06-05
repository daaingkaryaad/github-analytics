import type { GitHubUser } from '../types/github';

interface Props { user: GitHubUser }

export function UserCard({ user }: Props) {
  return (
    <div className="flex gap-5 p-5 bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 shadow-sm">
      <img src={user.avatar_url} alt={user.login} className="w-20 h-20 rounded-full flex-shrink-0" />
      <div className="flex flex-col justify-center gap-1">
        <h2 className="text-xl font-semibold">{user.name ?? user.login}</h2>
        {user.bio && <p className="text-sm text-gray-500 dark:text-gray-400">{user.bio}</p>}
        <div className="flex gap-5 mt-2 text-sm text-gray-600 dark:text-gray-300">
          <span><strong>{user.public_repos}</strong> repos</span>
          <span><strong>{user.followers}</strong> followers</span>
          <span><strong>{user.following}</strong> following</span>
        </div>
        <a href={user.html_url} target="_blank" rel="noreferrer" className="text-xs text-blue-500 hover:underline mt-1">
          github.com/{user.login}
        </a>
      </div>
    </div>
  );
}