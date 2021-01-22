import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
import i18n from '../i18n/i18n.js';

Vue.use(Vuetify);

export default new Vuetify({
    lang: {
        t: (key, ...params) => i18n.t(key, params),
    },
    theme: {
        options: {
            customProperties: true,
        },
        themes: {
            light: {
                primary: '#66BB6A', //green lighten-1
                secondary: '#29B6F6', //light-blue lighten-1
                // accent: '#8c9eff',
                error: '#b71c1c',
                background: '#ededed'
            },
        },
    },
});
