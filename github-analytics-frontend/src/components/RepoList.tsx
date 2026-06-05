import type { GitHubRepo } from '../types/github';

interface Props { repos: GitHubRepo[] }

export function RepoList({ repos }: Props) {
  const sorted = [...repos]
    .filter(r => !r.fork)
    .sort((a, b) => b.stargazers_count - a.stargazers_count)
    .slice(0, 10);

  return (
    <div className="flex flex-col gap-3">
      {sorted.map(repo => (
        <div key={repo.id} className="p-4 bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 shadow-sm">
          <div className="flex items-start justify-between gap-2">
            <a href={repo.html_url} target="_blank" rel="noreferrer" className="text-sm font-medium text-blue-600 hover:underline">
              {repo.name}
            </a>
            <span className="text-xs text-gray-400 whitespace-nowrap">★ {repo.stargazers_count}</span>
          </div>
          {repo.description && (
            <p className="text-xs text-gray-500 dark:text-gray-400 mt-1 line-clamp-2">{repo.description}</p>
          )}
          <div className="flex gap-3 mt-2 text-xs text-gray-400">
            {repo.language && <span>{repo.language}</span>}
            <span>{repo.forks_count} forks</span>
          </div>
        </div>
      ))}
    </div>
  );
}