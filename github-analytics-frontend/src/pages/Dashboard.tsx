import { useState } from 'react';
import { SearchBar } from '../components/SearchBar';
import { UserCard } from '../components/UserCard';
import { RepoList } from '../components/RepoList';
import { LanguageChart } from '../components/LanguageChart';
import { RepoStarsChart } from '../components/RepoStarsChart';
import { CommitActivityChart } from '../components/CommitActivityChart';
import { githubApi } from '../services/githubApi';
import { getCommits } from '../services/commitApi';

import type {
  GitHubUser,
  GitHubRepo,
  UserStats,
  Commit,
} from '../types/github';

export function Dashboard() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [user, setUser] = useState<GitHubUser | null>(null);
  const [repos, setRepos] = useState<GitHubRepo[]>([]);
  const [stats, setStats] = useState<UserStats | null>(null);
  const [commits, setCommits] = useState<Commit[]>([]);
  const [selectedRepo, setSelectedRepo] = useState('');

  const handleSearch = async (username: string) => {
    setLoading(true);
    setError(null);
    setUser(null);
    setRepos([]);
    setStats(null);
    setCommits([]);
    setSelectedRepo('');

    try {
      const [userData, reposData, statsData] = await Promise.all([
        githubApi.getUser(username),
        githubApi.getRepos(username),
        githubApi.getStats(username),
      ]);

      setUser(userData);
      setRepos(reposData);
      setStats(statsData);

      if (reposData.length > 0) {
        const firstRepo = reposData[0].name;

        setSelectedRepo(firstRepo);

        const commitData = await getCommits(
          username,
          firstRepo
        );

        setCommits(commitData);
      }
    } catch (e) {
      setError(
        e instanceof Error
          ? e.message
          : 'Something went wrong'
      );
    } finally {
      setLoading(false);
    }
  };

  const handleRepoChange = async (
    repoName: string
  ) => {
    if (!user) return;

    setSelectedRepo(repoName);

    try {
      const commitData = await getCommits(
        user.login,
        repoName
      );

      setCommits(commitData);
    } catch {
      setCommits([]);
    }
  };

  return (
    <div>
      <div className="max-w-4xl mx-auto px-4 py-12 flex flex-col gap-8">
        <div className="flex flex-col gap-2">
          <h1 className="text-2xl font-bold">
            GitHub Analytics
          </h1>

          <p className="text-sm text-gray-500">
            Search any GitHub user to see their stats
          </p>
        </div>

        <SearchBar
          onSearch={handleSearch}
          loading={loading}
        />

        {error && (
          <div className="px-4 py-3 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg text-sm text-red-700 dark:text-red-300">
            {error}
          </div>
        )}

        {user && (
          <>
            <UserCard user={user} />

            {stats && (
              <div className="grid grid-cols-3 gap-4">
                {[
                  {
                    label: 'Total stars',
                    value: stats.totalStars,
                  },
                  {
                    label: 'Public repos',
                    value: stats.totalRepos,
                  },
                  {
                    label: 'Languages',
                    value: Object.keys(stats.languages)
                      .length,
                  },
                ].map(({ label, value }) => (
                  <div
                    key={label}
                    className="p-4 bg-white dark:bg-gray-800 rounded-xl text-center border border-gray-100 dark:border-gray-700 shadow-sm"
                  >
                    <div className="text-2xl font-bold">
                      {value}
                    </div>

                    <div className="text-xs text-gray-500 mt-1">
                      {label}
                    </div>
                  </div>
                ))}
              </div>
            )}

            <div className="grid md:grid-cols-2 gap-6">
              {stats && (
                <LanguageChart
                  languages={stats.languages}
                />
              )}

              <RepoStarsChart repos={repos} />
            </div>

            {repos.length > 0 && (
              <div className="p-4 bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 shadow-sm">
                <label className="block text-sm font-semibold mb-2">
                  Repository for commit analytics
                </label>

                <select
                  value={selectedRepo}
                  onChange={(e) =>
                    handleRepoChange(e.target.value)
                  }
                  className="w-full px-3 py-2 rounded-lg border border-gray-300 dark:border-gray-600 dark:bg-gray-900"
                >
                  {repos.map((repo) => (
                    <option
                      key={repo.id}
                      value={repo.name}
                    >
                      {repo.name}
                    </option>
                  ))}
                </select>
              </div>
            )}

            {commits.length > 0 && (
              <CommitActivityChart
                commits={commits}
              />
            )}

            <div>
              <h3 className="text-sm font-semibold mb-2">
                Top repositories
              </h3>

              <RepoList repos={repos} />
            </div>
          </>
        )}
      </div>
    </div>
  );
}