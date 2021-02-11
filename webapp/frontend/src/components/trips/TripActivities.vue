<template>
	<v-card>
		<error-dialog v-model="showError" :error="error"></error-dialog>
		<v-dialog
			v-model="createDialog"
			:persistent="loadingCreate"
			width="500"
		>
			<trip-activity-form
				@submit="createActivity"
				:loading="loadingCreate"
			></trip-activity-form>
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
						:loading="loading"
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
		tripId: String,
	},
	data() {
		return {
			activities: [],
			createDialog: false,
			loadingCreate: false,
			loading: true,
			error: null,
		};
	},
	computed: {
		showError: {
			get() {
				return !!this.error;
			},
			set() {
				this.error = null;
			},
		},
	},
	methods: {
		async createActivity(activity) {
			this.loadingCreate = true;

			try {
				const activityCreated = await this.$store.dispatch(
					"activity/create",
					{
						tripId: this.tripId,
						activity: {
							...activity,
						},
					}
				);

				this.activities.push(activityCreated);
				this.createDialog = false;
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_activities.create_error"
				);
			}

			this.loadingCreate = false;
		},
		async getActivities() {
			this.loading = true;

			try {
				const activities = await this.$store.dispatch(
					"activity/getByTrip",
					{
						tripId: this.tripId,
					}
				);

				this.activities = activities;
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_activities.get_activities_error"
				);
			}

			this.loading = false;
		},
	},
	created() {
		this.getActivities();
	},
};
</script>