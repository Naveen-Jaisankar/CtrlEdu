// src/App.tsx
import { ThemeProvider } from "./context/ThemeProvider";
// import useTheme from "./hooks/useTheme";
import "./assets/styles/_global.scss";
import LandingPage from "./features/landing/pages/LandingPage";

const AppContent = () => {
  return (
    <div>
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
