const path = require('path');

module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
    ? '/paw-2019a-4/'
    : '/',
  "transpileDependencies": [
    "vuetify"
  ],
  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'es',
      localeDir: 'locales',
      enableInSFC: true
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        'components': path.resolve(__dirname, 'src/components'),
        'views': path.resolve(__dirname, 'src/views')
      },
    },
  },
};
