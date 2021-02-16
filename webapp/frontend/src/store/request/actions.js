import Axios from "axios";

export default {
    async join(_, payload) {
        const tripId = payload.tripId;
        const response = await Axios.post(`/trip/${tripId}/join`, { message: payload.message });
        const joinRequest = response.data;
        return joinRequest;
    }
};