<template>
	<v-skeleton-loader v-if="loading" type="image, article">
	</v-skeleton-loader>
	<v-card elevation="0" rounded="0" v-else>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-dialog v-model="formDialog" :persistent="loadingEdit" width="500">
			<trip-form
				:name="trip.name"
				:description="trip.description"
				:startDate="trip.startDate"
				:endDate="trip.endDate"
				:loading="loadingEdit"
				edit
				@submit="update"
			></trip-form>
		</v-dialog>
		<v-img
			class="white--text align-end"
			height="200px"
			src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
		>
			<v-app-bar flat color="rgba(0, 0, 0, 0)">
				<v-toolbar-title class="white--text">{{
					trip.name
				}}</v-toolbar-title>
				<v-spacer></v-spacer>
				<v-menu bottom right v-if="actions">
					<template v-slot:activator="{ on, attrs }">
						<v-btn color="white" icon v-bind="attrs" v-on="on">
							<v-icon>mdi-dots-vertical</v-icon>
						</v-btn>
					</template>

					<v-list>
						<v-list-item @click="formDialog = true">
							<v-list-item-title>{{
								$t("components.trips.trip_card.edit")
							}}</v-list-item-title>
						</v-list-item>
					</v-list>
				</v-menu>
			</v-app-bar>
		</v-img>
		<v-card-text class="text--primary">
			<div>{{ trip.description }}</div>
		</v-card-text>
		<v-card-text class="text--primary">
			Desde {{ formatDateString(trip.startDate) }} hasta el
			{{ formatDateString(trip.endDate) }}
		</v-card-text>
	</v-card>
</template>

<script>
import getBrowserLocale from "../../i18n/get-user-locale";
import TripForm from "./TripForm.vue";

export default {
	components: { TripForm },
	props: {
		id: {
			type: String,
			required: true,
		},
		actions: Boolean,
	},
	data() {
		return {
			trip: null,
			loading: false,
			error: null,
			loadingEdit: false,
			formDialog: false,
		};
	},
	methods: {
		formatDateString(date) {
			const opt = {
				timeZone: "UTC",
			};

			return date
				? new Date(date).toLocaleDateString(getBrowserLocale(), opt)
				: "";
		},
		async getTrip() {
			this.loading = true;

			try {
				const trip = await this.$store.dispatch("trip/get", {
					tripId: this.id,
				});
				this.trip = trip;
			} catch (error) {
				if (error?.response?.status === 404) {
					this.$emit("notFound");
				}

				this.error = this.$t("components.trips.trip_card.error");
			}

			this.loading = false;
		},
		async update(tripUpdates) {
			this.loadingEdit = true;

			try {
				this.trip = await this.$store.dispatch("trip/update", {
					id: this.id,
					...tripUpdates,
				});
			} catch (error) {
				this.error = this.$t("components.trips.trip_card.error_update");
			}

			this.loadingEdit = false;
		},
	},
	created() {
		this.getTrip();
	},
};
</script>