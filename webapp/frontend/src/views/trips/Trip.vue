<template>
	<v-container fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center">
			<v-col cols="12" md="6">
				<v-card>
					<v-tabs
						color="secondary"
						background-color="primary"
						show-arrows
						fixed-tabs
						centered
					>
						<v-tab> {{ $t("views.trip.trip") }}</v-tab>
						<v-tab>{{ $t("views.trip.activities") }}</v-tab>
						<v-tab v-if="member">{{ $t("views.trip.members") }}</v-tab>
						<v-tabs-slider color="secondary"></v-tabs-slider>
						<v-tab-item>
							<v-container fluid>
								<v-row>
									<v-col cols="12" class="px-0 pt-0">
										<trip-card
											:trip="trip"
											:loading="loading"
											:request="request"
											:member="member"
											@exit="exit"
											@joined="joined"
											actions
										></trip-card>
									</v-col>
								</v-row>
							</v-container>
						</v-tab-item>
						<v-tab-item>
							<trip-activities
								v-if="trip"
								:url="trip.tripActivitiesUri"
								:from="trip.startDate"
								:to="trip.endDate"
								:role="role"
								ref="activities"
							></trip-activities>
						</v-tab-item>
						<v-tab-item>
							<trip-members
								v-if="trip"
								:membersUrl="trip.tripMembersUri"
								:joinRequestUrl="trip.tripJoinRequestUri"
								:role="role"
								ref="members"
							></trip-members>
						</v-tab-item>
					</v-tabs>
				</v-card>
			</v-col>
			<v-col cols="12" md="4">
				<v-card>
					<v-tabs
						color="secondary"
						background-color="primary"
						fixed-tabs
						centered
					>
						<v-tab>{{ $t("views.trip.comments") }}</v-tab>
						<v-tab-item>
							<trip-comments
								v-if="trip"
								:url="trip.tripCommentsUri"
								:role="role"
								ref="comments"
							></trip-comments>
						</v-tab-item>
					</v-tabs>
				</v-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripCard from "components/trips/TripCard.vue";
import TripActivities from "components/activity/TripActivities.vue";
import TripMembers from "components/member/TripMembers.vue";
import TripComments from "components/comment/TripComments.vue";
import { memberRoles } from "../../enums.js";

export default {
	components: {
		TripCard,
		TripActivities,
		TripMembers,
		TripComments,
	},
	props: {
		id: {
			type: String,
			required: true,
		},
	},
	data() {
		return {
			tripId: this.id,
			trip: null,
			member: null,
			request: null,
			loading: false,
			error: null,
		};
	},
	computed: {
		role() {
			return !this.member
				? null
				: memberRoles.find((x) => x.value === this.member.role);
		},
	},
	methods: {
		async get() {
			this.loading = true;

			try {
				this.trip = await this.$store.dispatch("trip/get", {
					tripId: this.id,
				});

				await this.setMember();
			} catch (error) {
				if (error?.response?.status === 404) {
					await this.$router.replace({ name: "TripNotFound" });
				} else {
					this.error = this.$t("views.trip.error");
				}
			}

			this.loading = false;
		},
		async setMember() {
			const members = await this.$store.dispatch(
				"member/checkIfUserIsMember",
				{ url: this.trip.tripMembersUri }
			);

			this.member = members?.members[0];
			if (!this.member) {
				await this.setRequest();
			}
		},
		async setRequest() {
			const requests = await this.$store.dispatch(
				"request/checkIfUserHasRequest",
				{
					url: this.trip.tripJoinRequestUri,
				}
			);

			this.request = requests[0];
		},
		exit() {
			this.member = null;

			if (this.$refs.comments) {
				this.$refs.comments.exit();
			}

			if (this.$refs.activities) {
				this.$refs.activities.exit();
			}

			if (this.$refs.members) {
				this.$refs.members.exit();
			}
		},
		joined(request) {
			this.request = request;
		},
	},
	created() {
		this.get();
	},
};
</script>