import Axios from "axios";

export default {
    async create(_, payload) {
        const response = await Axios.post('/trip', payload);
        const trip = response.data;
        return trip;
    }
};