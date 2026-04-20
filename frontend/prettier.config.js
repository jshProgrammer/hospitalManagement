/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  arrowParens: 'avoid',
  bracketSpacing: true,
  bracketSameLine: false,
  jsxSingleQuote: false,
  printWidth: 100,
  proseWrap: 'always',
  quoteProps: 'as-needed',
  semi: false,
  singleQuote: true,
  tabWidth: 2,
  trailingComma: 'es5',
  useTabs: false,
  plugins: ['prettier-plugin-tailwindcss'],
}

export default config