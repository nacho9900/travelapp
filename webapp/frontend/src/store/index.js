import Vue from 'vue';
import Vuex from 'vuex';
import authenticationModule from './authentication/index.js';
import tripModule from './trip/index.js';
import googleModule from './google/index.js';
import activityModule from './activity/index.js';
import memberModule from './member/index.js';
import requestModule from './request/index.js';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth: authenticationModule,
    trip: tripModule,
    google: googleModule,
    activity: activityModule,
    member: memberModule,
    request: requestModule
  }
});
