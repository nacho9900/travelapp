<template>
	<v-container class="px-0 pt-0" fluid>
		<v-row>
			<v-col cols="12" class="px-0 pt-0">
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