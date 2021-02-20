export default {
    isAuth(state) {
        return !!state.token;
    },
    hasUser(state) {
        return !!state.user;
    },
    user(state) {
        return state.user;
    },
    didAutologout(state) {
        return state.autologout;
    }
}