export default {
    setToken(state, payload) {
        state.token = payload.token;
    },
    setUser(state, payload) {
        state.user = payload.user;
    },
    setAutologout(state, payload) {
        state.autologout = payload.autologout;
    }
}