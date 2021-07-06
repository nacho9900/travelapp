<template>
	<v-container class="px-0 pt-0" fluid>
		<v-row>
			<v-col cols="12" class="px-0 pt-0">
				<trip-paginated-list
					:search-criteria="searchCriteria"
					:welcome="welcome"
					@search="search"
				></trip-paginated-list>
			</v-col>
			<v-col cols="col-12" v-if="welcome">
				<v-container class="px-12" fluid>
					<v-row>
						<v-col cols="12">
							<h1 class="font-weight-regular">
								{{ $t("view.home.welcome_title") }}
							</h1>
						</v-col>
						<v-col cols="12">
							<h3 class="font-weight-regular">
								{{ $t("view.home.welcome_subtitle") }}
							</h3>
						</v-col>
						<v-col cols="4" class="d-flex justify-center">
							<v-img
								:src="require('@/assets/party.jpg')"
								style="border-radius: 50px"
								height="400"
							></v-img>
						</v-col>
						<v-col cols="4" class="d-flex justify-center">
							<v-img
								:src="require('@/assets/sky.jpg')"
								style="border-radius: 50px"
								height="400"
							></v-img>
						</v-col>
						<v-col cols="4" class="d-flex justify-center">
							<v-img
								:src="require('@/assets/safari.jpg')"
								style="border-radius: 50px"
								height="400"
							></v-img>
						</v-col>
					</v-row>
				</v-container>
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
	data() {
		return {
			welcome: true,
		};
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
	methods: {
		search() {
			this.welcome = false;
		},
	},
};
</script>