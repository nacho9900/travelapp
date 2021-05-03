<template>
	<v-container fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center">
			<v-col cols="12" class="mt-5"> 
                <h2 class="font-weight-regular">{{$t("views.user_trips.title")}}</h2>
            </v-col>
		</v-row>
		<v-row justify="center">
			<v-col cols="12" class="my-5" v-if="loading || hasTrips">
				<trip-data-iterator
					:trips="trips"
					:loading="loading"
          :link="link"
					:page="page"
					@next="nextPage"
					@prev="prevPage"
				></trip-data-iterator>
			</v-col>
			<v-col v-else cols="8" class="mt-16">
				<v-alert type="info">
					{{ $t("views.user_trips.no_trips") }}
					<router-link :to="{ name: 'Home' }" style="color: white">
						{{ $t("views.user_trips.search") }}
					</router-link>
					{{ "" + $t("views.user_trips.or") }}
					<router-link :to="{ name: 'TripNew' }" style="color: white">
						{{ $t("views.user_trips.create_a_new_one") }}
					</router-link>
				</v-alert>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripDataIterator from "components/trips/TripDataIterator.vue";
import parseLinkHeader from "parse-link-header";

export default {
	components: {
		TripDataIterator,
	},
	data() {
		return {
			trips: [],
			page: 1,
			loading: false,
			error: null,
      link: null,
		};
	},
	computed: {
		hasTrips() {
			return !!this.trips && this.trips.length > 0;
		},
	},
	methods: {
		async getAll(url) {
			this.loading = true;
			this.trips = [];

			try {
				const response = await this.$store.dispatch("trip/getUserTrips", {
					page: this.page,
          url: url,
				});

        const data = response.data;
        const trips = data.trips;

        this.trips = trips || [];

        if (this.hasTrips) {
          this.link = parseLinkHeader(response.headers["link"]);
        }

			} catch (error) {
				this.error = this.$t("views.user_trips.error");
			}

			this.loading = false;
		},
    async nextPage() {
      this.page = parseInt(this.link.next.page);
      await this.getAll(this.link.next.url);
    },
    async prevPage() {
      this.page = parseInt(this.link.prev.page);
      await this.getAll(this.link.prev.url);
    },
	},
	created() {
		this.getAll();
	},
};
</script>