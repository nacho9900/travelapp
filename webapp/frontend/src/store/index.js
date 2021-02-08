import Vue from 'vue';
import Vuex from 'vuex';
import authenticationModule from './authentication/index.js';
import tripModule from './trip/index.js';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth: authenticationModule,
    trip: tripModule
  }
});
