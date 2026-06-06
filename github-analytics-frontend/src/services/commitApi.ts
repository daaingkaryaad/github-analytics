export interface Commit {
  sha: string;
  commit: {
    author: {
      name: string;
      date: string;
    };
    message: string;
  };
}

export async function getCommits(
  username: string,
  repo: string
): Promise<Commit[]> {
  const res = await fetch(
    `/api/users/${username}/repos/${repo}/commits`
  );

  if (!res.ok) {
    throw new Error('Failed to fetch commits');
  }

  return res.json();
}