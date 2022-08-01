module.exports = (api) => {
    const tailwindcss = require('./tailwind.config.cjs')(api)
    const plugins = {
      'postcss-import': {},
      'tailwindcss/nesting': {},
      tailwindcss,
      autoprefixer: {}
    }
    if (api.mode === 'production') {
      plugins.cssnano = {}
    }
  
    return {
      plugins
    }
  }

