import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
} from 'recharts';

interface Repo {
  name: string;
  stargazers_count: number;
}

interface Props {
  repos: Repo[];
}

export function RepoStarsChart({ repos }: Props) {
  const data = [...repos]
    .sort((a, b) => b.stargazers_count - a.stargazers_count)
    .slice(0, 5)
    .map((repo) => ({
      name: repo.name,
      stars: repo.stargazers_count,
    }));

  if (data.length === 0) return null;

  return (
    <div className="p-5 bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 shadow-sm">
      <h3 className="text-sm font-semibold mb-4">
        Top repositories by stars
      </h3>

      <ResponsiveContainer width="100%" height={250}>
        <BarChart data={data}>
          <XAxis dataKey="name" hide />
          <YAxis />
          <Tooltip />
          <Bar dataKey="stars" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}