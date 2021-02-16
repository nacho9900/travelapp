<template>
	<div>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-dialog v-model="joinDialog" width="400" :persistent="loadingJoin">
			<trip-join-form
				:loading="loadingJoin"
				@submit="submitJoin"
			></trip-join-form>
		</v-dialog>
		<v-btn
			:color="buttonColor"
			:disabled="buttonDisabled"
			@click="click"
			depressed
			small
		>
			<v-icon left>{{ buttonIcon }}</v-icon>
			{{ buttonText }}
		</v-btn>
	</div>
</template>

<script>
import TripJoinForm from "./TripJoinForm.vue";

export default {
	components: {
		TripJoinForm,
	},
	props: {
		id: Number,
		status: String,
		member: Boolean,
	},
	data() {
		return {
			joinDialog: false,
			loadingJoin: false,
			error: null,
		};
	},
	computed: {
		buttonText() {
			if (this.member) {
				return this.$t("components.trips.trip_join_button.exit_trip");
			} else if (!this.status) {
				return this.$t("components.trips.trip_join_button.join");
			} else if (this.status === "PENDING") {
				return this.$t("components.trips.trip_join_button.pending");
			}

			return "";
		},
		buttonColor() {
			if (this.member) {
				return "error";
			}

			return "primary";
		},
		buttonDisabled() {
			return !this.member && !!this.status;
		},
		buttonIcon() {
			if (this.member) {
				return "mdi-logout";
			} else if (!this.status) {
				return "mdi-account-plus";
			} else if (this.status === "PENDING") {
				return "mdi-account-clock";
			}

			return "";
		},
	},
	methods: {
		click() {
			if (this.member) {
				//exit
			} else {
				this.joinDialog = true;
			}
		},
		async submitJoin(data) {
			this.loadingJoin = true;

			const payload = {
				...data,
				tripId: this.id,
			};

			try {
				const joinRequest = await this.$store.dispatch(
					"request/join",
					payload
				);
				this.$emit("joined", joinRequest);
				console.log(joinRequest);
				this.joinDialog = false;
			} catch (error) {
				this.error = this.$t("components.trips.trip_join_form.error");
			}

			this.loadingJoin = false;
		},
	},
};
</script>