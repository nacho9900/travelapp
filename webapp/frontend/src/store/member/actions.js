import Axios from "axios";

export default {
    async getAll(_, payload) {
        const response = await Axios.get(payload.url);
        const members = response.data;
        return members;
    },
    async exit(_, payload) {
        await Axios.post(payload.url);
    },
    async delete(_, payload) {
        await Axios.delete(payload.url);
    },
    async checkIfUserIsMember(context, payload) {
        const email = context.rootGetters.user.email;
        const data = {
            params: {
                email
            }
        }

        try {
            const response = await Axios.get(payload.url, data);
            return response.data;
        } catch (error) {
            if(error?.response?.status === 401) {
                return null;
            }
            throw error;
        }
    }
};