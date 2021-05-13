<template>
	<v-container class="px-0 py-0" fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<delete-dialog
			v-model="deleteMemberDialog"
			:title="$t('components.trips.trip_member_card.delete_dialog_title')"
			:message="deleteMemberMessage"
			:loading="memberDeleteLoading"
			@remove="deleteMember"
			@cancel="memberToDelete = null"
		></delete-dialog>
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
								:actions="canEditMember"
								@delete="setMemberToDelete"
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

export default {
	components: {
		TripMemberList,
		TripJoinRequestList,
	},
	props: {
		membersUrl: String,
		joinRequestUrl: String,
		role: Object,
	},
	data() {
		return {
			members: [],
			requests: [],
			loading: false,
			error: null,
			loadingRequests: false,
			memberToDelete: null,
			memberDeleteLoading: false,
		};
	},
	computed: {
		isMember() {
			return !!this.role;
		},
		canEditMember() {
			return this.role?.canEditMember;
		},
		deleteMemberDialog() {
			return !!this.memberToDelete;
		},
		memberToDeleteName() {
			if (!this.memberToDelete) {
				return "";
			}

			return (
				this.memberToDelete.user.firstname +
				" " +
				this.memberToDelete.user.lastname
			);
		},
		deleteMemberMessage() {
			if (!this.deleteMemberDialog) {
				return "";
			}

			return (
				this.$t(
					"components.trips.trip_member_card.delete_dialog_message"
				) + this.memberToDeleteName
			);
		},
	},
	methods: {
		async getMembers() {
			this.loading = true;

			try {
				const members = await this.$store.dispatch("member/getAll", {
					url: this.membersUrl,
				});
				this.members = members.members;
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
					{ url: this.joinRequestUrl }
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
		setMemberToDelete(data) {
			this.memberToDelete = this.members.find((x) => x.id === data.id);
		},
		async deleteMember() {
			this.memberDeleteLoading = true;

			try {
				await this.$store.dispatch("member/delete", {
					url: this.memberToDelete.memberUri,
				});
				this.members = this.members.filter(
					(x) => x.id !== this.memberToDelete.id
				);
				this.memberToDelete = null;
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_members.delete_error"
				);
			}

			this.memberDeleteLoading = false;
		},
	},
	created() {
		this.getMembers();
		this.getJoinRequests();
	},
};
</script>