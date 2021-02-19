<template>
	<v-container class="px-0" fluid>
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
		<v-row class="overflow-y-auto" style="max-height: 300px" v-else>
			<v-col cols="12" class="py-2 px-2" v-for="member in members" :key="member.id">
				<trip-member-card
					:id="member.id"
					:firstname="member.user.firstname"
					:lastname="member.user.lastname"
					:biography="member.user.biography"
					:role="member.role"
					:actions="actions"
				></trip-member-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripMemberCard from "components/member/TripMemberCard.vue";

export default {
	components: {
		TripMemberCard,
	},
	props: {
		members: Array,
		loading: Boolean,
		actions: Boolean,
	},
};
</script>