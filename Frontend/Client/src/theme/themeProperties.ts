const themeProperties = {
  defaultFontFamily:
    '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol"',
  fontSizes: {
    fontXXSmall: "10px",
    fontXSmall: "12px",
    fontSmall: "14px",
    fontSixteen: "16px",
    fontMedium: "18px",
    fontLarge: "22px",
    fontXLarge: "26px",
    fontXXLarge: "36px",
    fontXXXLarge: "48px",
    //to be converted to same unit
    fontRemLarge: "2.25rem",
    fontRemMedium: "1.5rem",
    fontRemSmall: "1.125rem",
    fontRemXSmall: "0.875rem",
  },
  colors: {
    primary: "#68B030", // Main primary color for buttons and accents
    primaryDark: "#007a49", // Darker variant for hover effects
    primaryLight: "#79AE4633", // Lighter variant for backgrounds or highlights
    secondary: "#ecb641", // Secondary accent color
    background: "#ffffff", // Default background color
    backgroundDark: "#343a40", // Darker background color for dark mode
    textPrimary: "#2f3336", // Main text color
    textSecondary: "#555555", // Secondary text color for less emphasis
    divider: "#d5d5d5", // Color for dividers and borders
    danger: "#e74c3c", // Error or danger color
    success: "#2980b9", // Success or confirmation color
  },

  // Light and dark theme variations
  lightTheme: {
    background: "#ffffff",
    primaryFontColor: "#2f3336",
    divider: "#d5d5d5",
    highlightBackground: "#f4ffec",
    disabledFontColor: "#909090",
    backgroundBase: "#FaFaFf",
  },
  darkTheme: {
    background: "#343a40",
    primaryFontColor: "#ffffff",
    divider: "#101113",
    highlightBackground: "#202020",
    disabledFontColor: "#686868",
    backgroundBase: "#202020",
  },
};

export default themeProperties;
