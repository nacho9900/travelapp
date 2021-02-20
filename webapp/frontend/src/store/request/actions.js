import Axios from "axios";

export default {
    async join(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.post(`/trip/${tripId}/join`, { message: payload.message });
        const joinRequest = response.data;
        return joinRequest;
    },
    async getAllPending(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.get(`/trip/${tripId}/join`);
        const joinRequests = response.data;
        return joinRequests;
    },
    async accept(_, payload) {
        const tripId = payload.tripId;
        const requestId = payload.id;
        const response = await Axios.post(`/trip/${tripId}/join/${requestId}/accept`);
        const member = response.data;
        return member;
    },
    async reject(_, payload) {
        const tripId = payload.tripId;
        const requestId = payload.id;
        await Axios.post(`/trip/${tripId}/join/${requestId}/reject`);
    }
};