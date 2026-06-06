import { AuthBar } from './components/AuthBar';
import { Dashboard } from './pages/Dashboard';

export default function App() {
  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
      <AuthBar />
      <Dashboard />
    </div>
  );
}