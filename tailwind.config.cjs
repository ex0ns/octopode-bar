
const colors = require('tailwindcss/colors')

module.exports = ({env}) => {
  const scalajsMode = env === 'production' ? './out/app/fullLinkJS.dest/main.js' : './out/app/fastLinkJS.dest/main.js'

  return {
    content: [
      scalajsMode
    ],
    theme: {
      extend: {
        colors: {
          gray: colors.gray,
          'purple': {
            dark: '#3e0189',
            light: '#7f66ff'
          }
        },
        fontFamily: {
          condensed: [
            'Roboto Condensed'
          ]
        },
      },
      backgroundColor: theme => ({
        ...theme('colors'),
        'orange': '#fc4a1a',
      })
    },
    plugins: [
      require('@tailwindcss/forms'),
      require('@tailwindcss/typography'),
      require('@tailwindcss/aspect-ratio')
    ],
  }
}


