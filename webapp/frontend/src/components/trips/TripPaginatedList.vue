<template>
	<v-container fluid>
		<v-img height="400" :src="require('@/assets/search-background.jpg')">
			<v-row justify="center" class="px-10 mt-10">
				<v-col cols="12">
					<trip-search-bar @submit="search"></trip-search-bar>
				</v-col>
			</v-row>
		</v-img>
		<v-row justify="center" class="mt-5">
			<v-col v-if="!welcome" class="px-10" cols="12">
				<v-alert v-if="noResults" type="info">
					{{ $t("components.trips.trip_paginated_list.no_results") }}
				</v-alert>
				<trip-data-iterator
					:trips="trips"
					:total="total"
					:itemsPerPage="itemsPerPage"
					:loading="loading"
					:page="page"
					@next="nextPage"
					@prev="prevPage"
					v-else
				></trip-data-iterator>
			</v-col>
			<v-col cols="12" v-else>
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
			searchCriteria: null,
			loading: false,
			noResults: false,
			welcome: true,
		};
	},
	methods: {
		async search(data) {
			this.welcome = false;
			this.noResults = false;
			this.searchCriteria = data;
			this.page = 1;

			await this.serachTrips();
		},
		async nextPage() {
			this.page += 1;

			await this.serachTrips();
		},
		async prevPage() {
			this.page -= 1;

			await this.serachTrips();
		},
		async serachTrips() {
			this.loading = true;
			this.trips = [];

			const payload = {
				...this.searchCriteria,
				page: this.page,
			};

			try {
				const result = await this.$store.dispatch(
					"trip/search",
					payload
				);
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
	},
};
</script>