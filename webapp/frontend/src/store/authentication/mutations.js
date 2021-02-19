export default {
    setToken(state, payload) {
        state.token = payload.token;
    },
    setUser(state, payload) {
        if (payload) {
            state.user = { ...payload };
        } else {
            state.user = null;
        }
    },
    setAutologout(state, payload) {
        state.autologout = !!payload.autologout;
    }
}