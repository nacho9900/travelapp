import Axios from "axios";

export default {
    async join(_, payload) {
        const response = await Axios.post(payload.url, { message: payload.message });
        const joinRequest = response.data;
        return joinRequest;
    },
    async getAllPending(_, payload) {
        const response = await Axios.get(payload.url);
        const joinRequests = response.data;
        return joinRequests;
    },
    async accept(_, payload) {
        const response = await Axios.post(payload.url);
        const member = response.data;
        return member;
    },
    async reject(_, payload) {
        await Axios.post(payload.url);
    },
    async checkIfUserHasRequest(context, payload) {
        const email = context.rootGetters.user.email;
        const data = {
            params: {
                email
            }
        };
        
        const response = await Axios.get(payload.url, data);
        return response.data;
    }
};