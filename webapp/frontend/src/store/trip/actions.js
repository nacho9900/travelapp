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
    async search(_, payload) {
        const data = {
            params: {
                text: payload.text,
                from: payload.from,
                to: payload.to,
                latitude: payload.location?.latitude,
                longitude: payload.location?.longitude,
                page: payload.page
            }
        };
        const response = await Axios.get("/trip/search", data);
        const result = response.data;
        return result;
    },
    async getUserTrips(_, payload) {
        const data = {
            params: {
                page: payload.page,
            }
        };

        const response = await Axios.get("/trip", data);
        const result = response.data;
        return result;
    }
};