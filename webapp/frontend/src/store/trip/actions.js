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
        const response = await Axios.put(payload.url, payload);
        const trip = response.data;
        return trip;
    },
    async uploadImage(_, payload) {
        const image = payload.image;
        const response = await Axios.put(payload.url, image);
        const location = response.data;
        return location;
    },
    async search(_, payload) {
        if (payload.url) {
            return await Axios.get(payload.url);
        } else {
            const data = {
                params: {
                    text: payload.text,
                    from: payload.from,
                    to: payload.to,
                    latitude: payload.location?.latitude,
                    longitude: payload.location?.longitude,
                    page: payload.page,
                    perPage: 12,
                }
            };

            return await Axios.get("/trip/search", data);
        }
    },
    async getUserTrips(_, payload) {
        if (payload.url) {
            return await Axios.get(payload.url);
        } else {
            const data = {
                params: {
                    page: payload.page,
                    perPage: 12,
                }
            };

            return await Axios.get("/trip", data);
        }
    }
};