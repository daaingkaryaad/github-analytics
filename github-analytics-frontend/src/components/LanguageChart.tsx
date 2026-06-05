import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const COLORS = ['#3B82F6','#10B981','#F59E0B','#EF4444','#8B5CF6',
                 '#EC4899','#14B8A6','#F97316'];

interface Props { languages: Record<string, number> }

export function LanguageChart({ languages }: Props) {
  const data = Object.entries(languages)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 8)
    .map(([name, value]) => ({ name, value }));

  if (data.length === 0) return null;

  return (
    <div className="p-5 bg-white dark:bg-gray-800 rounded-xl
                    border border-gray-100 dark:border-gray-700 shadow-sm">
      <h3 className="text-sm font-semibold mb-4">Top languages</h3>
      <ResponsiveContainer width="100%" height={220}>
        <PieChart>
          <Pie
            data={data}
            cx="50%"
            cy="50%"
            innerRadius={55}
            outerRadius={85}
            paddingAngle={3}
            dataKey="value"
          >
            {data.map((_, i) => (
              <Cell key={i} fill={COLORS[i % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip formatter={(v: number) => [`${v} repos`, '']} />
          <Legend />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
}