<template>
	<v-container fluid>
		<v-img height="400" :src="require('@/assets/search-background.jpg')">
			<v-row justify="center" class="px-10 mt-10">
				<v-col cols="12">
					<trip-search-bar
						@submit="search"
						:from="from"
						:to="to"
						:location="location"
						ref="tripSearchBar"
					></trip-search-bar>
				</v-col>
			</v-row>
		</v-img>
		<v-row justify="center" class="mt-5">
			<v-col v-if="!welcome" class="px-10" cols="12">
				<v-alert v-if="!hasResults && !loading" type="info">
					{{ $t("components.trips.trip_paginated_list.no_results") }}
				</v-alert>
				<trip-data-iterator
					:trips="trips"
					:link="link"
					:page="page"
					:loading="loading"
					@next="nextPage"
					@prev="prevPage"
					v-else
				></trip-data-iterator>
			</v-col>
			<v-col cols="12" v-else> </v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripSearchBar from "components/trips/TripSearchBar.vue";
import TripDataIterator from "components/trips/TripDataIterator.vue";
import parseLinkHeader from "parse-link-header";
import VueRouter from "vue-router";

export default {
	components: {
		TripSearchBar,
		TripDataIterator,
	},
	props: {
		searchCriteria: Object,
		welcome: Boolean,
	},
	data() {
		return {
			trips: [],
			page: 1,
			searchCriteriaEntered: null,
			loading: false,
			hasResults: true,
			link: null,
			searchOnMounted: false,
		};
	},
	computed: {
		from() {
			return this.searchCriteriaEntered?.from;
		},
		to() {
			return this.searchCriteriaEntered?.to;
		},
		location() {
			return this.searchCriteriaEntered?.location;
		},
	},
	methods: {
		async search(data) {
			this.$emit("search");
			this.searchCriteriaEntered = data;

			if (this.searchOnMounted) {
				this.searchOnMounted = false;
			} else {
				this.page = 1;
			}

			await this.setCriteriaToPath(data);
			await this.searchTrips();
		},
		async setCriteriaToPath(data) {
			const query = {
				query: {},
			};

			if (data.from) {
				query.query.from = data.from;
			}

			if (data.to) {
				query.query.to = data.to;
			}

			if (data.location) {
				query.query.latitude = data.location.latitude;
				query.query.longitude = data.location.longitude;
				query.query.googleId = data.location.googleId;
				query.query.name = data.location.name;
				query.query.address = data.location.address;
			}

			if (this.page !== 1) {
				query.query.page = this.page;
			}

			// if (Object.keys(query.query).length > 0) {
			try {
				await this.$router.push({ ...query });
			} catch (e) {
				if (VueRouter.isNavigationFailure(e)) {
					//do nothing
				} else {
					throw e;
				}
			}
			// }
		},
		async nextPage() {
			this.page = parseInt(this.link.next.page);
			await this.setCriteriaToPath(this.searchCriteriaEntered);
			await this.searchTrips(this.link.next.url);
		},
		async prevPage() {
			this.page = parseInt(this.link.prev.page);
			await this.setCriteriaToPath(this.searchCriteriaEntered);
			await this.searchTrips(this.link.prev.url);
		},
		async searchTrips(url) {
			this.loading = true;

			const payload = {
				...this.searchCriteriaEntered,
				page: this.page,
				url: url,
			};

			try {
				const response = await this.$store.dispatch(
					"trip/search",
					payload
				);

				const data = response.data;
				const trips = data.trips;

				this.hasResults = trips?.length > 0;

				if (this.hasResults) {
					this.link = parseLinkHeader(response.headers["link"]);
				}

				this.trips = trips || [];
			} catch (error) {
				//
			}

			this.loading = false;
		},
		async setSearchCriteria() {
			this.searchCriteriaEntered = { ...this.searchCriteria };
			if (this.searchCriteria.page) {
				this.page = parseInt(this.searchCriteria.page, 10);
			}
			this.searchOnMounted = true;
		},
	},
	async mounted() {
		if (Object.keys(this.searchCriteria).length > 0) {
			await this.setSearchCriteria();
			this.$refs.tripSearchBar.submit();
		}
	},
};
</script>