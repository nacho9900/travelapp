<template>
	<v-container fluid>
		<v-row v-if="loading">
			<v-col cols="12">
				<v-skeleton-loader type="sentences"></v-skeleton-loader>
			</v-col>
			<v-col cols="12">
				<v-skeleton-loader type="sentences"></v-skeleton-loader>
			</v-col>
			<v-col cols="12">
				<v-skeleton-loader type="sentences"></v-skeleton-loader>
			</v-col>
		</v-row>
		<v-row v-else-if="isEmpty">
			<v-col cols="12" class="d-flex justify-center">
				<v-alert type="info">
					{{ $t("components.trips.trip_join_request_list.empty") }}
				</v-alert>
			</v-col>
		</v-row>
		<v-row class="overflow-y-auto" style="max-height: 300px" v-else>
			<v-col cols="12" v-for="request in requests" :key="request.id">
				<trip-join-request-card
					:id="request.id"
					:tripId="tripId"
					:firstname="request.user.firstname"
					:lastname="request.user.lastname"
					:biography="request.user.biography"
					:message="request.message"
					@accept="accept"
					@reject="reject"
				>
				</trip-join-request-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripJoinRequestCard from "components/request/TripJoinRequestCard.vue";
export default {
	components: { TripJoinRequestCard },
	props: {
		tripId: String,
		requests: Array,
		loading: Boolean,
	},
	computed: {
		isEmpty() {
			return this.requests && this.requests.length === 0;
		},
	},
	methods: {
		accept(data) {
			this.$emit("accept", data);
		},
		reject(data) {
			this.$emit("reject", data);
		},
	},
};
</script>