// src/App.tsx
import { ThemeProvider, useTheme } from "./context/ThemeProvider";
// import useTheme from "./hooks/useTheme";
import "./assets/styles/_global.scss";
import LandingPage from "./features/landing/pages/LandingPage";

const AppContent = () => {
  const { toggleTheme } = useTheme();

  return (
    <div>
      <button onClick={toggleTheme}>Toggle Theme</button>
      <LandingPage />
    </div>
  );
};

const App = () => (
  <ThemeProvider>
    <AppContent />
  </ThemeProvider>
);

export default App;
