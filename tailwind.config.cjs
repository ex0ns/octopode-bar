
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
        },
        gridTemplateColumns:{
          'table': 'minmax(8em, 25%) minmax(50%, 75%)'
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


