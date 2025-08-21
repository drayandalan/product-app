import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default ({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const api = env.VITE_API_URL || 'http://localhost:8080'

  return defineConfig({
    plugins: [react()],
    server: {
      proxy: {
        '/api': { target: api, changeOrigin: true }
      }
    },
    build: { outDir: 'dist' }
  })
}
