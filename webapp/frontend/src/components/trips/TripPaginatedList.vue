<template>
  <v-container fluid>
    <v-img height="400"
           :src="require('@/assets/search-background.jpg')">
      <v-row justify="center"
             class="px-10 mt-10">
        <v-col cols="12">
          <trip-search-bar @submit="search"></trip-search-bar>
        </v-col>
      </v-row>
    </v-img>
    <v-row justify="center"
           class="mt-5">
      <v-col v-if="!welcome"
             class="px-10"
             cols="12">
        <v-alert v-if="!hasResults && !loading"
                 type="info">
          {{$t("components.trips.trip_paginated_list.no_results")}}
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
      <v-col cols="12"
             v-else>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import TripSearchBar from "components/trips/TripSearchBar.vue";
import TripDataIterator from "components/trips/TripDataIterator.vue";
import parseLinkHeader from "parse-link-header";

export default {
  components: {
    TripSearchBar,
    TripDataIterator,
  },
  data() {
    return {
      trips: [],
      page: 1,
      searchCriteria: null,
      loading: false,
      hasResults: true,
      welcome: true,
      link: null,
    };
  },
  methods: {
    async search(data) {
      this.welcome = false;
      this.searchCriteria = data;
      this.page = 1;

      await this.searchTrips();
    },
    async nextPage() {
      this.page = parseInt(this.link.next.page);
      await this.searchTrips(this.link.next.url);
    },
    async prevPage() {
      this.page = parseInt(this.link.prev.page);
      await this.searchTrips(this.link.prev.url);
    },
    async searchTrips(url) {
      this.loading = true;

      const payload = {
        ...this.searchCriteria,
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

        this.trips = trips || [];

        this.hasResults = this.trips.length > 0;

        if (this.hasResults) {
          this.link = parseLinkHeader(response.headers["link"]);
        }
      } catch (error) {
        //
      }

      this.loading = false;
    },
  },
};
</script>