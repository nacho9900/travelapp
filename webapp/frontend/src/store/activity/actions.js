import Axios from "axios";

export default {
    async getByTrip(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.get(`/trip/${tripId}/activity`);
        const activities = response.data;
        return activities;
    },
    async create(_, payload) {
        const tripId = payload.tripId;
        const activity = {
            ...payload.activity
        };
        const response = await Axios.post(`/trip/${tripId}/activity`, activity);
        const activityCreated = response.data;
        return activityCreated;
    },
    async update(_, payload) {
        const tripId = payload.tripId;
        const activity = {
            ...payload.activity
        };
        const response = await Axios.put(`/trip/${tripId}/activity`, activity);
        const activityUpdated = response.data;
        return activityUpdated;
    }
};