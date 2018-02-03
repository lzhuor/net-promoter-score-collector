// https://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parserOptions: {
      parser: 'babel-eslint',
      sourceType: 'module'
  },
  env: {
    browser: true,
  },
  // https://github.com/standard/standard/blob/master/docs/RULES-en.md
  extends: [
      'plugin:vue/recommended',
  ],
  // required to lint *.vue files
  // plugins: [
  //   'html'
  // ],
  // add your custom rules here
  rules: {
    // allow async-await
    'generator-star-spacing': 'off',
    // allow debugger during development
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    "vue/html-indent": ["error", 4, {
      "attribute": 1,
      "closeBracket": 0,
      "alignAttributesVertically": true,
      "ignores": []
    }]
  }
}
