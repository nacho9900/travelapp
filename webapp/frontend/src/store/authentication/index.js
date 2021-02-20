import actions from './actions.js';
import mutations from './mutations.js';
import getters from './getters.js';

export default {
    state: {
        token: null,
        user: null,
        autologout: false,
    },
    actions,
    mutations,
    getters,
};