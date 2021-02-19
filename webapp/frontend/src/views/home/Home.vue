<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12">
				<trip-paginated-list></trip-paginated-list>
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
};
</script>