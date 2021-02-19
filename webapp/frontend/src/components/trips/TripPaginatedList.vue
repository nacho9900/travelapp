<template>
	<v-container fluid>
		<v-row justify="center" class="mt-10">
			<v-col cols="12">
				<trip-search-bar @submit="search"></trip-search-bar>
			</v-col>
		</v-row>
		<v-row justify="center" class="mt-16">
			<v-col cols="12">
				<v-alert v-if="noResults" type="info">
					{{ $t("components.trips.trip_paginated_list.no_results") }}
				</v-alert>
				<trip-data-iterator
					:trips="trips"
					:total="total"
					:hasNextPage="hasNextPage"
					:hasPrevPage="hasPrevPage"
					:itemsPerPage="itemsPerPage"
					:loading="loading"
					:page="page"
					v-else
				></trip-data-iterator>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripSearchBar from "components/trips/TripSearchBar.vue";
import TripDataIterator from "components/trips/TripDataIterator.vue";

export default {
	components: {
		TripSearchBar,
		TripDataIterator,
	},
	data() {
		return {
			trips: [],
			total: 0,
			page: 1,
			itemsPerPage: 12,
			hasNextPage: false,
			searchData: null,
			loading: false,
			noResults: false,
		};
	},
	computed: {
		hasPrevPage() {
			return this.page > 1;
		},
	},
	methods: {
		async search(data) {
			this.noResults = false;
			this.loading = true;
			this.searchData = data;
			this.page = 1;
			this.trips = [];

			try {
				const result = await this.$store.dispatch("trip/search", data);
				this.itemsPerPage = result.itemsPerPage;
				this.total = result.total;
				this.trips = result.trips;
				this.hasNextPage = result.hasNextPages;
				this.noResults = this.trips.length === 0;
			} catch (error) {
				//
			}

			this.loading = false;
		},
		nextPage() {},
		prevPage() {},
	},
};
</script>