// vite.config.ts
import { defineConfig } from "vite";
import reactRefresh from "@vitejs/plugin-react-refresh";

export default defineConfig({
  plugins: [reactRefresh()],
  css: {
    preprocessorOptions: {
      scss: {},
    },
  },
  server: {
    watch: {
      usePolling: true,
    },
    fs: {
      allow: [
        './', // Allow the project root directory
        'node_modules' // Allow access to node_modules
      ],
    },
  },
});
