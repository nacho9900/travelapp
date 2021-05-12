import Axios from "axios";

export default {
    async get(_, payload) {
        const response = await Axios.get(payload.url);
        const activities = response.data;
        return activities;
    },
    async create(_, payload) {
        const activity = {
            ...payload.activity
        };
        const response = await Axios.post(payload.url, activity);
        const activityCreated = response.data;
        return activityCreated;
    },
    async update(_, payload) {
        const activity = {
            ...payload.activity
        };
        const response = await Axios.put(activity.activityUri, activity);
        const activityUpdated = response.data;
        return activityUpdated;
    },
    async delete(_, payload) {
        const response = await Axios.delete(payload.activity.activityUri);

        if (response.status !== 200) {
            throw new Error();
        }
    }
};