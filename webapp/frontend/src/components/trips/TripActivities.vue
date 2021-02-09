<template>
	<v-container class="pt-0 px-0" fluid>
		<v-row>
			<v-col cols="12" class="pa-0">
				<trip-activities-map
					:activities="activities"
				></trip-activities-map>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12">
				<trip-activities-list
					:activities="activities"
				></trip-activities-list>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripActivitiesMap from "./TripActivitiesMap.vue";
import TripActivitiesList from "./TripActivitiesList.vue";

export default {
	components: {
		TripActivitiesMap,
		TripActivitiesList,
	},
	props: {
		activities: Array,
	},
};
</script>