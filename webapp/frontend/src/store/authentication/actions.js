import Axios from "axios";

let timer;

export default {
    async login(context, payload) {
        const data = {
            email: payload.email,
            password: payload.password
        };

        const response = await Axios.post("/login", data);

        const auth = response.data;

        const expirationDate = new Date(auth.expiresIn).getTime();
        const expiresIn = expirationDate - new Date().getTime();
        const userId = auth.userId;

        const token = auth.token;

        localStorage.setItem('token', token);
        localStorage.setItem('tokenExpiration', expirationDate);
        localStorage.setItem('userId', userId);
        Axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        timer = setTimeout(() => {  //Para autologout cuando se acaba el token
            context.dispatch('autologout');
        }, expiresIn);

        context.commit('setToken', {token});
        await context.dispatch('loadUser');
    },
    tryLogin(context) {
        if (!context.getters.isAuth) {
            const token = localStorage.getItem('token');
            const tokenExpiration = localStorage.getItem('tokenExpiration');

            if (!token || !tokenExpiration) {
                return;
            }

            const expiresIn = +tokenExpiration - new Date().getTime();

            if (expiresIn < 0) {
                return;
            }

            timer = setTimeout(() => {  //Para autologout cuando se acaba el token
                context.dispatch('autologout');
            }, expiresIn);

            Axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            context.commit('setToken', {token: token});
            context.commit('setAutologout', {autologout: true});
            context.dispatch('loadUser')
        }
    },
    autologout(context) {
        context.commit('setAutologout', {autologout: true});
        context.dispatch('logout');
    },
    logout(context) {
        context.commit('setToken', {token: null});
        context.commit('setUser', null);

        delete Axios.defaults.headers.common['Authorization'];

        localStorage.removeItem('token');
        localStorage.removeItem('tokenExpiration');
        localStorage.removeItem('userId');

        clearTimeout(timer);
    },
    async signup(_, payload) {
        const user = {
            ...payload
        };

        const response = await Axios.post("/auth/signup", user);
        const userCreated = response.data;
        return userCreated;
    },
    async loadUser(context) {
        const userId = localStorage.getItem('userId');
        const response = await Axios.get(`/users/${userId}`);
        const user = response.data;
        context.commit("setUser", user);
    },
    async updateUser(context, payload) {
        const user = {...payload};
        const response = await Axios.put(`/users/${user.id}`, user);
        const userUpdated = response.data;
        context.commit('setUser', userUpdated);
        return userUpdated;
    },
    async changePassword(context, payload) {
        const user = context.getters.user;
        const passwords = {
            ...payload
        };
        await Axios.post(`/users/${user.id}/change-password`, passwords);
    },
    async passwordRecovery(_, payload) {
        await Axios.post("/auth/password-recovery", payload);
    },
    async changePasswordRecovery(_, payload) {
        await Axios.post("/auth/change-password", payload);
    },
    async changeAvatar(context, payload) {
        const user = context.getters.user;
        const image = payload.image;
        await Axios.put(`/users/${user.id}/picture`, image);
    },
    async verify(_, payload) {
        const token = payload.token;
        await Axios.post("/auth/verify", {token: token});
    }
};