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
    },
    async update(_, payload) {
        const tripId = payload.id;
        const response = await Axios.put(`/trip/${tripId}`, payload);
        const trip = response.data;
        return trip;
    },
    async uploadImage(_, payload) {
        const tripId = payload.id;
        const image = payload.image;
        const response = await Axios.put(`/trip/${tripId}/picture`, image);
        const location = response.data;
        return location;
    },
};