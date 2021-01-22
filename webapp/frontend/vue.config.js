const path = require('path');

module.exports = {
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
  }
}
