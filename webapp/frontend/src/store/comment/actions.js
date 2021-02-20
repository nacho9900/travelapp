import Axios from "axios";

export default {
    async create(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.post(`/trip/${tripId}/comment`, { comment: payload.comment });
        const comment = response.data;
        return comment;
    },
    async getAll(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.get(`/trip/${tripId}/comment`);
        const comments = response.data;
        return comments;
    }
};