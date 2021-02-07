import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import i18n from './i18n/i18n.js';
import Axios from 'axios';
import * as GmapVue from 'gmap-vue';

import ErrorDialog from 'components/UI/ErrorDialog.vue';
import DeleteDialog from 'components/UI/DeleteDialog.vue';
import LoadingDialog from 'components/UI/LoadingDialog.vue';
import DatePicker from 'components/UI/DatePicker.vue';
import ImageForm from 'components/UI/ImageForm.vue';
import PasswordTextField from 'components/UI/PasswordTextField.vue';

Vue.config.productionTip = false;

Axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL;


Vue.use(GmapVue, {
  load: {
    key: process.env.VUE_APP_MAPS_API_KEY,
    libraries: 'places',
    installComponents: true
  }
});

const app = new Vue({
  router,
  store,
  vuetify,
  i18n,
  render: h => h(App)
});

Vue.component('error-dialog', ErrorDialog);
Vue.component('delete-dialog', DeleteDialog);
Vue.component('loading-dialog', LoadingDialog);
Vue.component('date-picker', DatePicker);
Vue.component('image-form', ImageForm);
Vue.component('password-text-field', PasswordTextField);

app.$mount('#app');