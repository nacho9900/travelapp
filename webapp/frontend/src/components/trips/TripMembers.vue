<template>
	<v-container fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row>
			<v-col cols="12">
				<trip-member-list
					v-if="isMember"
					:members="members"
					:loading="loading"
					:actions="showActions"
				></trip-member-list>
				<v-alert type="info" text dense v-else>
					{{ $t("components.trips.trip_members.not_members") }}
				</v-alert>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import { memberRoles } from "../../enums.js";
import TripMemberList from "./TripMemberList.vue";

export default {
	components: {
		TripMemberList,
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
			role: null,
			loading: false,
			error: null,
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
		setRole(role) {
			if (role) {
				this.role = memberRoles.find((x) => x.value === role);
			}
		},
	},
	created() {
		this.getMembers();
	},
};
</script>