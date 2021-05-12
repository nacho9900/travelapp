import Axios from "axios";

export default {
    async create(_, payload) {
        const response = await Axios.post(payload.url, { comment: payload.comment });
        const comment = response.data;
        return comment;
    },
    async getAll(_, payload) {
        const response = await Axios.get(payload.url);
        const comments = response.data;
        return comments;
    }
};