import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
} from 'recharts';

interface Commit {
  sha: string;
  commit: {
    author: {
      date: string;
    };
  };
}

interface Props {
  commits: Commit[];
}

export function CommitActivityChart({ commits }: Props) {
  const counts = commits.reduce<Record<string, number>>((acc, commit) => {
    const day = commit.commit.author.date.slice(0, 10);

    acc[day] = (acc[day] || 0) + 1;

    return acc;
  }, {});

  const data = Object.entries(counts)
    .map(([date, commits]) => ({
      date,
      commits,
    }))
    .sort((a, b) => a.date.localeCompare(b.date));

  if (data.length === 0) return null;

  return (
    <div className="p-5 bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 shadow-sm">
      <h3 className="text-sm font-semibold mb-4">
        Commit activity
      </h3>

      <ResponsiveContainer width="100%" height={250}>
        <LineChart data={data}>
          <XAxis dataKey="date" />
          <YAxis />
          <Tooltip />
          <Line
            type="monotone"
            dataKey="commits"
            strokeWidth={2}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}