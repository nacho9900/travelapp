<template>
	<v-card :loading="loading">
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-card-title class="font-weight-regular pb-2">
			{{ fullname }}
		</v-card-title>
		<v-card-text class="text--primary py-2" v-if="biography">
			{{ biography }}
		</v-card-text>
		<v-card-text
			v-if="message"
			class="d-flex justify-center text--primary font-italic py-2"
		>
			<p class="text-center">"{{ message }}"</p>
		</v-card-text>
		<v-card-actions class="px-4">
			<v-btn :disabled="loading" @click="accept" small color="primary">
				<v-icon left> mdi-account-plus </v-icon>
				{{ $t("components.trips.trip_join_request_card.accept") }}
			</v-btn>
			<v-btn :disabled="loading" @click="reject" small color="error">
				<v-icon left> mdi-logout </v-icon>
				{{ $t("components.trips.trip_join_request_card.reject") }}
			</v-btn>
		</v-card-actions>
	</v-card>
</template>

<script>
export default {
	props: {
		id: Number,
		tripId: String,
		firstname: String,
		lastname: String,
		biography: String,
		message: String,
	},
	data() {
		return {
			loading: false,
			error: null,
		};
	},
	computed: {
		fullname() {
			return this.firstname + " " + this.lastname;
		},
	},
	methods: {
		async accept() {
			this.loading = true;

			try {
				const member = await this.$store.dispatch("request/accept", {
					tripId: this.tripId,
					id: this.id,
				});
				this.$emit("accept", { id: this.id, member: member });
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_join_request_card.accept_error"
				);
			}

			this.loading = false;
		},
		async reject() {
			this.loading = true;

			try {
				await this.$store.dispatch("request/reject", {
					tripId: this.tripId,
					id: this.id,
				});
				this.$emit("reject", { id: this.id });
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_join_request_card.accept_error"
				);
			}

			this.loading = false;
		},
	},
};
</script>