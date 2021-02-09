<template>
	<v-container fluid>
		<v-row>
			<v-col>
				<gmap-map
					:center="{ lat: 10, lng: 10 }"
					:zoom="7"
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
};
</script>