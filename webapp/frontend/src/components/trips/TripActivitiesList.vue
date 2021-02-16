<template>
	<v-skeleton-loader v-if="loading" type="article"></v-skeleton-loader>
	<v-container v-else fluid>
		<v-row v-if="activities && activities.length > 0">
			<v-col
				cols="12"
				class="py-1"
				v-for="activity in activities"
				:key="activity.id"
			>
				<trip-activity-card
					:id="activity.id"
					:name="activity.name"
					:address="activity.place.address"
					:category="activity.category"
					:endDate="activity.endDate"
					:startDate="activity.startDate"
					:actions="actions"
					@edit="edit"
					@remove="remove"
				></trip-activity-card>
			</v-col>
		</v-row>
		<v-row v-else>
			<v-col cols="12" class="d-flex justify-center">
				<v-alert type="info" text dense>
					{{ $t("components.trips.trip_activities_list.no_data") }}
				</v-alert>
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
		loading: Boolean,
		actions: Boolean,
	},
	methods: {
		edit(data) {
			this.emitActivity(data.id, "edit");
		},
		remove(data) {
			this.emitActivity(data.id, "remove");
		},
		emitActivity(id, action) {
			const activity = this.activities.find((x) => x.id === id);
			this.$emit(action, activity);
		},
	},
};
</script>