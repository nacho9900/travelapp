<template>
	<v-container fluid>
		<error-dialog v-model="showError" :error="error"></error-dialog>
		<v-dialog v-model="success" width="300" persistent>
			<v-card>
				<v-card-title>
					{{ $t("views.trip_new.success_title") }}
				</v-card-title>
				<v-card-text>
					{{ $t("views.trip_new.success_text") }}
				</v-card-text>
				<v-card-actions>
					<v-container fluid>
						<v-row justify="end">
							<v-col class="d-flex justify-end">
								<v-btn color="primary" :to="tripPath">{{
									$t("views.trip_new.success_btn")
								}}</v-btn>
							</v-col>
						</v-row>
					</v-container>
				</v-card-actions>
			</v-card>
		</v-dialog>
		<v-row justify="center">
			<v-col cols="12" lg="6" md="9">
				<trip-form
					@submit="create"
					:loading="loading"
					:activity-warning="true"
				></trip-form>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripForm from "components/trips/TripForm.vue";

export default {
	components: {
		TripForm,
	},
	data() {
		return {
			error: null,
			success: false,
			trip: null,
			loading: false,
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
		tripPath() {
			const tripId = this.trip?.id;
			return `/trip/${tripId}`;
		},
	},
	methods: {
		async create(trip) {
			this.loading = true;

			try {
				this.trip = await this.$store.dispatch("trip/create", trip);
				this.success = true;
			} catch (error) {
				this.error = this.$t("views.trip_new.error");
			}

			this.loading = false;
		},
	},
};
</script>