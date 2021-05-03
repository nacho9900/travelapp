<template>
  <v-progress-circular v-if="loading"
                       size="100"
                       color="primary"
                       indeterminate></v-progress-circular>
  <v-data-iterator
      :items="trips"
      :items-per-page="itemsPerPage"
      :page="page"
      no-data-text=""
      hide-default-footer
      disable-filtering
      disable-sort
      v-else
  >
    <template v-slot:default="props">
      <v-row>
        <v-col
            v-for="trip in props.items"
            :key="trip.id"
            cols="12"
            sm="6"
            md="4"
            lg="3"
        >
          <trip-simple-card
              :id="trip.id"
              :name="trip.name"
              :description="trip.description"
              :from="trip.startDate"
              :to="trip.endDate"
          ></trip-simple-card>
        </v-col>
      </v-row>
    </template>
    <template v-slot:loading>
      <v-row justify="center"
             align="center"
             class="ma-16">
        <v-progress-circular
            color="primary"
            indeterminate
            size="100"
        ></v-progress-circular>
      </v-row>
    </template>
    <template v-if="hasMoreThanOnePage"
              v-slot:footer>
      <v-card color="primary">
        <v-row class="mt-16"
               align="center"
               justify="center">
          <v-spacer></v-spacer>

          <span class="mr-4">
						Page {{page}} of {{numberOfPages}}
					</span>
          <v-btn
              fab
              dark
              color="secondary"
              class="mr-1"
              :disabled="!hasPrevPage || loading"
              @click="prevPage"
          >
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <v-btn
              fab
              dark
              color="secondary"
              class="ml-1"
              :disabled="!hasNextPage || loading"
              @click="nextPage"
          >
            <v-icon>mdi-chevron-right</v-icon>
          </v-btn>
        </v-row>
      </v-card>
    </template>
  </v-data-iterator>
</template>

<script>
import TripSimpleCard from "components/trips/TripSimpleCard.vue";

export default {
  props: {
    trips: Array,
    page: Number,
    link: Object,
    loading: Boolean,
  },
  components: {
    TripSimpleCard,
  },
  computed: {
    numberOfPages() {
      return this.link.last.page;
    },
    hasNextPage() {
      return this.page !== this.link.last.page;
    },
    hasPrevPage() {
      return !!this.link.prev;
    },
    itemsPerPage() {
      return parseInt(this.link.first.perPage);
    },
    hasMoreThanOnePage() {
      return this.link.first.page !== this.link.last.page;
    }
  },
  methods: {
    nextPage() {
      this.$emit("next");
    },
    prevPage() {
      this.$emit("prev");
    },
  },
};
</script>