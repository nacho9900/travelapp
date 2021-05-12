<template>
	<v-card>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-dialog v-model="formDialog" :persistent="loadingAction" width="500">
			<trip-activity-form
				@submit="submitActivityForm"
				:loading="loadingAction"
				:edit="editDialog"
				:name="activityToEdit ? activityToEdit.name : null"
				:place="activityToEdit ? activityToEdit.place : null"
				:startDate="activityToEdit ? activityToEdit.startDate : null"
				:endDate="activityToEdit ? activityToEdit.endDate : null"
				:from="from"
				:to="to"
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

export default {
	components: {
		TripActivitiesMap,
		TripActivitiesList,
		TripActivityForm,
	},
	props: {
		url: String,
		from: String,
		to: String,
		role: Object,
	},
	data() {
		return {
			activities: [],
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
						url: this.url,
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
				activityUri: this.activityToEdit.activityUri,
			};

			try {
				const activityUpdated = await this.$store.dispatch(
					"activity/update",
					{
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
				const activities = await this.$store.dispatch("activity/get", {
					url: this.url,
				});

				this.activities = activities.activities;
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_activities.get_activities_error"
				);
			}

			this.loading = false;
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