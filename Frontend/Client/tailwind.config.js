/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,ts,jsx,tsx}"],
  darkMode: "media",
  theme: {
    extend: {
      colors: {
        neutral: {
          900: "#1A202C",
          400: "#CBD5E0",
        },
      },
    },
  },
  plugins: [],
};
