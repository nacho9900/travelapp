<template>
	<v-container fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row>
			<v-col cols="12">
				<trip-member-list
					:members="members"
					:loading="loading"
				></trip-member-list>
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
				this.error = this.$t("components.trips.trip_members.get_error");
			}

			this.loading = false;
		},
		setRole(role) {
			if(role) {
				this.role = memberRoles.find(x => x.value === role);
			}
		}
	},
	created() {
		this.getMembers();
	},
};
</script>