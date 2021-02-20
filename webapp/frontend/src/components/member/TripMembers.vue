<template>
	<v-container class="px-0 py-0" fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center">
			<v-col cols="12">
				<v-expansion-panels focusable accordion>
					<v-expansion-panel>
						<v-expansion-panel-header
							>{{ $t("components.trips.trip_members.members") }}
							<template v-if="loading" v-slot:actions>
								<v-progress-circular
									color="primary"
									indeterminate
								>
								</v-progress-circular>
							</template>
						</v-expansion-panel-header>
						<v-expansion-panel-content>
							<trip-member-list
								v-if="isMember || loading"
								:members="members"
								:loading="loading"
								:actions="showActions"
							></trip-member-list>
							<v-alert class="mt-10" type="info" v-else>
								{{
									$t(
										"components.trips.trip_members.not_members"
									)
								}}
							</v-alert>
						</v-expansion-panel-content>
					</v-expansion-panel>
					<v-expansion-panel>
						<v-expansion-panel-header disable-icon-rotate
							>{{
								$t("components.trips.trip_members.join_request")
							}}
							<template v-slot:actions>
								<v-progress-circular
									v-if="loadingRequests"
									color="primary"
									indeterminate
								>
								</v-progress-circular>
								<v-chip v-else color="warning">
									{{ requests.length }}
								</v-chip>
							</template>
						</v-expansion-panel-header>
						<v-expansion-panel-content>
							<trip-join-request-list
								v-if="canEditMember"
								:tripId="tripId"
								:requests="requests"
								:loading="loadingRequests"
								@accept="accept"
								@reject="reject"
							></trip-join-request-list>
							<v-alert class="mt-10" type="info" v-else>
								{{
									$t(
										"components.trips.trip_members.not_admin"
									)
								}}
							</v-alert>
						</v-expansion-panel-content>
					</v-expansion-panel>
				</v-expansion-panels>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripMemberList from "components/member/TripMemberList.vue";
import TripJoinRequestList from "components/request/TripJoinRequestList.vue";
import { memberRoles } from "../../enums.js";

export default {
	components: {
		TripMemberList,
		TripJoinRequestList,
	},
	props: {
		tripId: {
			required: true,
			type: String,
		},
	},
	data() {
		return {
			members: [],
			requests: [],
			role: null,
			loading: false,
			error: null,
			loadingRequests: false,
			memberRoles,
		};
	},
	computed: {
		isMember() {
			return !!this.role;
		},
		showActions() {
			return this.isMember && this.role.canEditMember;
		},
		canEditMember() {
			return !!this.role && this.role.canEditMember;
		},
	},
	methods: {
		async getMembers() {
			this.loading = true;

			try {
				const members = await this.$store.dispatch("member/getAll", {
					tripId: this.tripId,
				});
				this.members = members.members;
				const role = members.role;
				this.setRole(role);
			} catch (error) {
				if (!this.error?.response?.status === 404) {
					this.error = this.$t(
						"components.trips.trip_members.get_error"
					);
				}
			}

			this.loading = false;
		},
		async getJoinRequests() {
			this.loadingRequests = true;

			try {
				this.requests = await this.$store.dispatch(
					"request/getAllPending",
					{ tripId: this.tripId }
				);
			} catch (error) {
				if (!this.error?.response?.status == 401) {
					this.error = this.$t(
						"components.trips.trip_members.get_requests_error"
					);
				}
			}

			this.loadingRequests = false;
		},
		setRole(role) {
			if (role) {
				this.role = memberRoles.find((x) => x.value === role);
			}
		},
		accept(data) {
			const id = data.id;
			const member = data.member;
			this.requests = this.requests.filter((x) => x.id !== id);
			this.members.push(member);
		},
		reject(data) {
			const id = data.id;
			this.requests = this.requests.filter((x) => x.id !== id);
		},
		exit() {
			this.getMembers();
			this.getJoinRequests();
		},
	},
	created() {
		this.getMembers();
		this.getJoinRequests();
	},
};
</script>