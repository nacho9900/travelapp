<template>
	<v-card>
		<v-dialog v-model="createDialog" :persistent="loading" width="500" >
			<trip-activity-form :loading="loading"></trip-activity-form>
		</v-dialog>
		<v-container class="pt-0 px-0" fluid>
			<v-row>
				<v-col cols="12" class="pa-0">
					<trip-activities-map
						:activities="activities"
					></trip-activities-map>
				</v-col>
			</v-row>
			<v-row>
				<v-col
					cols="12"
					style="max-height: 400px"
					class="overflow-y-auto"
				>
					<trip-activities-list
						:activities="activities"
					></trip-activities-list>
				</v-col>
			</v-row>
		</v-container>
		<v-card-actions>
			<v-container class="pt-0 px-0" fluid>
				<v-row justify="end">
					<v-col cols="6" class="d-flex justify-end px-4">
						<v-btn color="primary" @click="createDialog = true">{{
							$t("components.trips.trip_activities.new_activity")
						}}</v-btn>
					</v-col>
				</v-row>
			</v-container>
		</v-card-actions>
	</v-card>
</template>

<script>
import TripActivitiesMap from "./TripActivitiesMap.vue";
import TripActivitiesList from "./TripActivitiesList.vue";
import TripActivityForm from "./TripActivityForm.vue";

export default {
	components: {
		TripActivitiesMap,
		TripActivitiesList,
		TripActivityForm,
	},
	props: {
		activities: Array,
		loading: Boolean,
	},
	data() {
		return {
			createDialog: false,
		};
	},
};
</script>