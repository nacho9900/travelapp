<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" class="py-1" v-for="activity in activities" :key="activity.id">
				<trip-activity-card
					:id="activity.id"
					:name="activity.name"
					:address="activity.place.address"
					:category="activity.category"
					:endDate="activity.endDate"
					:startDate="activity.startDate"
				></trip-activity-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripActivityCard from "./TripActivityCard.vue";

export default {
	components: {
		TripActivityCard,
	},
	props: {
		activities: Array,
	},
};
</script>