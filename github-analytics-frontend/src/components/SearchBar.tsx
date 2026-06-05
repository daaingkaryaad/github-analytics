import { useState } from 'react';

interface Props {
  onSearch: (username: string) => void;
  loading: boolean;
}

export function SearchBar({ onSearch, loading }: Props) {
  const [value, setValue] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (value.trim()) onSearch(value.trim());
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-2 w-full max-w-xl">
      <input
        value={value}
        onChange={e => setValue(e.target.value)}
        placeholder="Enter a GitHub username..."
        className="flex-1 px-4 py-2 rounded-lg border border-gray-200 dark:border-gray-700
                   bg-white dark:bg-gray-800 text-sm outline-none
                   focus:ring-2 focus:ring-blue-500 transition"
      />
      <button
        type="submit"
        disabled={loading || !value.trim()}
        className="px-5 py-2 bg-blue-600 hover:bg-blue-700 disabled:opacity-50
                   text-white text-sm rounded-lg transition"
      >
        {loading ? 'Searching…' : 'Search'}
      </button>
    </form>
  );
}