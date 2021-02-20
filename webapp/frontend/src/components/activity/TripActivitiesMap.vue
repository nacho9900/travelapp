<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12">
				<gmap-map
					ref="gmap"
					:center="{
						lat: -34.61,
						lng: -58.48,
					}"
					:zoom="12"
					style="width: 100%; height: 300px"
				>
					<gmap-marker
						v-for="activity in activities"
						:key="activity.id"
						:position="
							google &&
							new google.maps.LatLng(
								activity.place.latitude,
								activity.place.longitude
							)
						"
					>
					</gmap-marker>
				</gmap-map>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import { gmapApi } from "gmap-vue";

export default {
	props: {
		activities: Array,
	},
	computed: {
		google: gmapApi,
	},
	methods: {
		initialize() {
			this.$refs.gmap.$mapPromise.then((map) => {
				if (this.activities && this.activities.length > 0) {
					var bounds = new this.google.maps.LatLngBounds();
					for (const index in this.activities) {
						const activity = this.activities[index];
						const place = activity.place;
						bounds.extend({
							lat: place.latitude,
							lng: place.longitude,
						});
					}
					map.fitBounds(bounds);

					if (map.getZoom() >= 22) {
						map.setZoom(16);
					}
				}
			});
		},
	},
	watch: {
		activities() {
			this.initialize();
		},
	},
	mounted() {
		this.initialize();
	},
};
</script>