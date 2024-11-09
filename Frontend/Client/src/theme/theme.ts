// getGlobalStyles.ts

import {
  lightTheme,
  darkTheme,
  fontSizes,
  colors,
  defaultFontFamily,
} from "./themeProperties";

export const getGlobalStyles = (isDarkMode: boolean) => {
  const theme = isDarkMode ? darkTheme : lightTheme;

  return `
    :root {
      --primary-font-family: ${defaultFontFamily};

      /* Font Sizes */
      --font-xx-small: ${fontSizes.fontXXSmall};
      --font-x-small: ${fontSizes.fontXSmall};
      --font-small: ${fontSizes.fontSmall};
      --font-sixteen: ${fontSizes.fontSixteen};
      --font-medium: ${fontSizes.fontMedium};
      --font-large: ${fontSizes.fontLarge};
      --font-x-large: ${fontSizes.fontXLarge};
      --font-xx-large: ${fontSizes.fontXXLarge};
      --font-xxx-large: ${fontSizes.fontXXXLarge};

      /* Colors */
      --primary-color: ${colors.primary};
      --primary-font-color: ${theme.primaryFontColor};
      --primary-dark-color: ${colors.primaryDark};
      --primary-light-color: ${colors.primaryLight};
      --secondary-color: ${colors.secondary};
      --background-color: ${theme.background};
      --divider-color: ${theme.divider};
      --highlight-background: ${theme.highlightBackground};
      --danger-color: ${colors.danger};
      --success-color: ${colors.success};
      --disabled-font-color: ${theme.disabledFontColor};
      --background-base: ${theme.backgroundBase};
    }
  `;
};
