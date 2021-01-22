import Axios from "axios";

export default {
    async login(context, payload) {
        const data = {
            email: payload.email,
            password: payload.password
        };

        const response = await Axios.post("/login", data);

        const auth = response.data;

        const expiresIn = +auth.expiresIn * 1000;
        const expirationDate = new Date().getTime() + expiresIn;

        const token = auth.token;

        localStorage.setItem('token', token);
        localStorage.setItem('tokenExpiration', expirationDate);
        Axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        context.commit("setToken", { token });
    },
    tryLogin(context) {
        if (!context.getters.isAuth) {
            const token = localStorage.getItem('token');
            // const userId = localStorage.getItem('userId');
            // const tokenExpiration = localStorage.getItem('tokenExpiration');

            // const expiresIn = +tokenExpiration - new Date().getTime();

            // if (expiresIn < 0) {
            //     return;
            // }

            // timer = setTimeout(() => {  //Para autologout cuando se acaba el token
            //     context.dispatch('autoLogout');
            // }, expiresIn);

            if (token) {//&& userId) {
                Axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                context.commit('setToken', {
                    token: token,
                });
            }
        }
    },
};