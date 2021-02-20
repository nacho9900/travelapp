<template>
	<v-card>
		<error-dialog v-model="showError" :error="error"></error-dialog>
		<v-dialog v-model="formDialog" :persistent="loadingAction" width="500">
			<trip-activity-form
				@submit="submitActivityForm"
				:loading="loadingAction"
				:edit="editDialog"
				:name="activityToEdit ? activityToEdit.name : null"
				:place="activityToEdit ? activityToEdit.place : null"
				:startDate="activityToEdit ? activityToEdit.startDate : null"
				:endDate="activityToEdit ? activityToEdit.endDate : null"
			></trip-activity-form>
		</v-dialog>
		<delete-dialog
			v-model="removeDialog"
			:title="$t('components.trips.trip_activities.remove_dialog_title')"
			:message="
				$t('components.trips.trip_activities.delete_dialog_message')
			"
			:loading="loadingDelete"
			@cancel="activityToRemove = null"
			@remove="removeActivity"
		></delete-dialog>
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
						:actions="showActions"
						@edit="(activity) => (activityToEdit = activity)"
						@remove="(activity) => (activityToRemove = activity)"
					></trip-activities-list>
				</v-col>
			</v-row>
		</v-container>
		<v-card-actions v-if="showActions">
			<v-container class="pt-0 px-0" fluid>
				<v-row>
					<v-col cols="6" class="px-4">
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
import TripActivitiesMap from "components/activity/TripActivitiesMap.vue";
import TripActivitiesList from "components/activity/TripActivitiesList.vue";
import TripActivityForm from "components/activity/TripActivityForm.vue";
import { memberRoles } from "../../enums.js";

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
			role: null,
			createDialog: false,
			loadingAction: false,
			loadingDelete: false,
			loading: true,
			error: null,
			activityToEdit: null,
			activityToRemove: null,
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
		editDialog: {
			get() {
				return !!this.activityToEdit;
			},
			set() {
				this.activityToEdit = null;
			},
		},
		removeDialog: {
			get() {
				return !!this.activityToRemove;
			},
			set() {
				this.activityToRemove = null;
			},
		},
		formDialog: {
			get() {
				return this.createDialog || this.editDialog;
			},
			set() {
				if (this.editDialog) {
					this.editDialog = false;
				} else {
					this.createDialog = false;
				}
			},
		},
		showActions() {
			return !!this.role && this.role.canEditActivity;
		},
	},
	methods: {
		async submitActivityForm(activity) {
			this.loadingAction = true;

			if (this.activityToEdit) {
				await this.updateActivity(activity);
			} else {
				await this.createActivity(activity);
			}

			this.loadingAction = false;
		},
		async createActivity(activity) {
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
		},
		async updateActivity(activity) {
			const activityToUpdate = {
				...activity,
				id: this.activityToEdit.id,
			};

			try {
				const activityUpdated = await this.$store.dispatch(
					"activity/update",
					{
						tripId: this.tripId,
						activity: activityToUpdate,
					}
				);

				this.activities = this.activities.filter(
					(x) => x.id !== activityUpdated.id
				);
				this.activities.push(activityUpdated);
				this.activityToEdit = null;
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_activities.update_error"
				);
			}
		},
		async removeActivity() {
			this.loadingDelete = true;

			try {
				await this.$store.dispatch("activity/delete", {
					tripId: this.tripId,
					activity: this.activityToRemove,
				});

				this.activities = this.activities.filter(
					(x) => x.id !== this.activityToRemove.id
				);
				this.activityToRemove = false;
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_activities.delete_error"
				);
			}

			this.loadingDelete = false;
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

				this.activities = activities.activities;
				const role = activities.role;
				this.setRole(role);
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_activities.get_activities_error"
				);
			}

			this.loading = false;
		},
		setRole(role) {
			if (role) {
				this.role = memberRoles.find((x) => x.value === role);
			}
		},
		exit() {
			this.getActivities();
		},
	},
	created() {
		this.getActivities();
	},
};
</script>