import Axios from "axios";

export default {
    async getAll(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.get(`/trip/${tripId}/member`);
        const members = response.data;
        return members;
    }
};