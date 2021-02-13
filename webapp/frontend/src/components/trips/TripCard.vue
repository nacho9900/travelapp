<template>
	<v-skeleton-loader v-if="loading" type="image, article">
	</v-skeleton-loader>
	<v-card elevation="0" rounded="0" v-else>
		<error-dialog v-model="showError" :error="error"></error-dialog>
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
				<v-btn color="white" icon>
					<v-icon>mdi-dots-vertical</v-icon>
				</v-btn>
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

export default {
	props: {
		id: {
			type: String,
			required: true,
		},
	},
	data() {
		return {
			trip: null,
			loading: false,
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
	},
	created() {
		this.getTrip();
	},
};
</script>