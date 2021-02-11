import Axios from "axios";

export default {
    async create(_, payload) {
        const response = await Axios.post('/trip', payload);
        const trip = response.data;
        return trip;
    },
    async get(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.get(`/trip/${tripId}`);
        const trip = response.data;
        return trip;
    }
};