<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" v-for="member in members" :key="member.id">
				<trip-member-card
					:id="member.id"
					:firstname="member.user.firstname"
					:lastname="member.user.lastname"
					:role="member.role"
				></trip-member-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripMemberCard from "./TripMemberCard.vue";

export default {
	components: {
		TripMemberCard,
	},
	props: {
		members: Array,
		loading: Boolean,
	},
};
</script>