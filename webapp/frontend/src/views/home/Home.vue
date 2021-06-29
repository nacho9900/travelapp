<template>
	<v-container class="px-0 pt-0" fluid>
		<v-row>
			<v-col cols="12" class="px-0 pt-0">
				<trip-paginated-list
					:search-criteria="searchCriteria"
				></trip-paginated-list>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripPaginatedList from "components/trips/TripPaginatedList.vue";

export default {
	components: {
		TripPaginatedList,
	},
	props: {
		from: String,
		to: String,
		location: Object,
	},
	computed: {
		searchCriteria() {
			const query = {};

			if (this.from) {
				query.from = this.from;
			}

			if (this.to) {
				query.to = this.to;
			}

			if (this.location) {
				query.location = { ...this.location };
			}

			return query;
		},
	},
};
</script>