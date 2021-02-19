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
import TripJoinForm from "components/request/TripJoinForm.vue";

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
			if (this.hasMember) {
				return this.$t("components.trips.trip_join_button.exit_trip");
			} else if (this.canJoin) {
				return this.$t("components.trips.trip_join_button.join");
			} else {
				return this.$t("components.trips.trip_join_button.pending");
			}
		},
		buttonColor() {
			if (this.hasMember) {
				return "error";
			}

			return "primary";
		},
		buttonDisabled() {
			return !this.hasMember && !this.canJoin;
		},
		buttonIcon() {
			if (this.hasMember) {
				return "mdi-logout";
			} else if (this.canJoin) {
				return "mdi-account-plus";
			} else {
				return "mdi-account-clock";
			}
		},
		hasMember() {
			return !!this.member;
		},
		canJoin() {
			return !this.status || this.status !== "PENDING";
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