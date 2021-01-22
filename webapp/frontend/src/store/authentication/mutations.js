export default {
    setToken(state, payload) {
        state.token = payload.token;
    },
    setUser(state, payload) {
        state.user = payload.user;
    }
}