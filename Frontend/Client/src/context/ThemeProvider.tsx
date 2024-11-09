// src/context/ThemeProvider.tsx

import React, { createContext, useContext, useState, useEffect } from "react";
import { getGlobalStyles } from "../theme/theme";

interface ThemeContextProps {
  isDarkMode: boolean;
  toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContextProps>({
  isDarkMode: false,
  toggleTheme: () => {},
});

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isDarkMode, setIsDarkMode] = useState(() => {
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme) return savedTheme === "dark";
    return (
      window.matchMedia &&
      window.matchMedia("(prefers-color-scheme: dark)").matches
    );
  });

  useEffect(() => {
    const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
    const systemThemeChangeListener = (e: MediaQueryListEvent) => {
      setIsDarkMode(e.matches);
      // Clear localStorage to prioritize system theme unless toggled manually
      localStorage.removeItem("theme");
    };
    mediaQuery.addEventListener("change", systemThemeChangeListener);
    return () => {
      mediaQuery.removeEventListener("change", systemThemeChangeListener);
    };
  }, []);

  useEffect(() => {
    const themeStyles = getGlobalStyles(isDarkMode);
    const styleTag = document.createElement("style");
    styleTag.id = "theme-styles";
    styleTag.textContent = themeStyles;
    document.head.appendChild(styleTag);
    return () => {
      const existingStyleTag = document.getElementById("theme-styles");
      if (existingStyleTag) document.head.removeChild(existingStyleTag);
    };
  }, [isDarkMode]);

  const toggleTheme = () => {
    const newTheme = !isDarkMode;
    setIsDarkMode(newTheme);
    localStorage.setItem("theme", newTheme ? "dark" : "light");
  };

  return (
    <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => useContext(ThemeContext);
