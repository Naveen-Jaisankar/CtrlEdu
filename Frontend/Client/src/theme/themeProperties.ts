// theme/themeProperties.ts

export const defaultFontFamily =
  '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"';

export const fontSizes = {
  fontXXSmall: "10px",
  fontXSmall: "12px",
  fontSmall: "14px",
  fontSixteen: "16px",
  fontMedium: "18px",
  fontLarge: "22px",
  fontXLarge: "26px",
  fontXXLarge: "36px",
  fontXXXLarge: "48px",
  fontRemLarge: "2.25rem",
  fontRemMedium: "1.5rem",
  fontRemSmall: "1.125rem",
  fontRemXSmall: "0.875rem",
};

export const colors = {
  primary: "#68B030",
  primaryDark: "#007a49",
  primaryLight: "#79AE4633",
  secondary: "#ecb641",
  background: "#ffffff",
  backgroundDark: "#343a40",
  textPrimary: "#2f3336",
  textSecondary: "#555555",
  divider: "#d5d5d5",
  danger: "#e74c3c",
  success: "#2980b9",
};

// Light theme
export const lightTheme = {
  defaultFontFamily,
  fontSizes,
  colors,
  background: "#ffffff",
  primaryFontColor: "#2f3336",
  divider: "#d5d5d5",
  highlightBackground: "#f4ffec",
  disabledFontColor: "#909090",
  backgroundBase: "#FaFaFf",
};

// Dark theme
export const darkTheme = {
  defaultFontFamily,
  fontSizes,
  colors,
  background: "#343a40",
  primaryFontColor: "#ffffff",
  divider: "#101113",
  highlightBackground: "#202020",
  disabledFontColor: "#686868",
  backgroundBase: "#202020",
};
