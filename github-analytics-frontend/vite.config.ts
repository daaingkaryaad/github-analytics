import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    // Dev: forward /api to the Spring Boot backend so the frontend
    // can use relative paths (same as the Nginx proxy in production).
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
});