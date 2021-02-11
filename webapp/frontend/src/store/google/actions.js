export default {
    async geocode(_, payload) {
        const google = payload.google;
        const geocoder = new google.maps.Geocoder();
        geocoder.geocode({ placeId: payload.googleId }, (results) => {
            const place = results[0];
            const latlng = {
                latitude: place.geometry.location.lat(),
                longitude: place.geometry.location.lng()
            };
            payload.callback(latlng);
        });
    },
    async searchPlace(_, payload) {
        const address = payload.address;

        if (!payload.address) {
            payload.callback([]);
            return;
        }

        const google = payload.google;

        const service = new google.maps.places.AutocompleteService();

        const query = {
            input: address,
        };

        service.getQueryPredictions(query, (predictions) => {
            const places = predictions ? predictions.map(x => {
                return {
                    name: x.description,
                    address: x.description,
                    googleId: x.place_id
                };
            }) : [];

            payload.callback(places);
        });
    }
};