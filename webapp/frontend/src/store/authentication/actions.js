import Axios from "axios";

let timer;

export default {
    async login(context, payload) {
        const data = {
            email: payload.email,
            password: payload.password
        };

        const response = await Axios.post("/auth/login", data);

        const auth = response.data;

        const expirationDate = auth.expiresIn;
        const expiresIn = expirationDate - new Date().getTime();

        const token = auth.token;

        localStorage.setItem('token', token);
        localStorage.setItem('tokenExpiration', expirationDate);
        Axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        timer = setTimeout(() => {  //Para autologout cuando se acaba el token
            context.dispatch('logout');
        }, expiresIn);

        context.commit("setToken", { token });
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
            context.commit('setToken', { token: token });
            context.commit('setAutologout', { autologout: true });
        }
    },
    autologout(context) {
        context.commit('setAutologout', { autologout: true });
        context.dispatch('logout');
    },
    logout(context) {
        context.commit('setToken', { token: null });
        context.commit('setUser', { user: null });

        localStorage.removeItem('token');
        localStorage.removeItem('tokenExpiration');

        clearTimeout(timer);
    },
    async signup(_, payload) {
        const user = {
            ...payload
        };

        const response = await Axios.post("/auth/signup", user);
        const userCreated = response.data;
        return userCreated;
    }
};