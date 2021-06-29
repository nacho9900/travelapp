<template>
	<v-autocomplete
		v-model="address"
		:search-input.sync="search"
		:loading="loading"
		:items="items"
		:dense="dense"
		:rules="rules"
		:disabled="disabled"
		:solo="solo"
		:label="label"
		:hide-details="hideDetails"
		:outlined="outlined"
		item-text="address"
		prepend-inner-icon="mdi-map-marker-outline"
		return-object
		clearable
		no-filter
	>
	</v-autocomplete>
</template>

<script>
import { gmapApi } from "gmap-vue";

export default {
	props: {
		value: Object,
		dense: Boolean,
		disabled: Boolean,
		rules: Array,
		solo: Boolean,
		label: String,
		hideDetails: Boolean,
		outlined: Boolean,
	},
	data() {
		return {
			address: null,
			search: null,
			items: [],
			loading: false,
		};
	},
	computed: {
		google: gmapApi,
	},
	methods: {
		async googleSearch() {
			this.loading = true;

			await this.$gmapApiPromiseLazy();

			this.$store.dispatch("google/searchPlace", {
				address: this.search,
				google: this.google,
				callback: (places) => {
					this.items = places;
					this.loading = false;
				},
			});
		},
		init() {
			if (this.value) {
				this.items = [this.value];
				this.address = this.value;
			}
		},
		async geocode() {
			await this.$gmapApiPromiseLazy();
			this.$store.dispatch("google/geocode", {
				...this.address,
				google: this.google,
				callback: (latlng) => {
					this.address.latitude = latlng.latitude;
					this.address.longitude = latlng.longitude;

					this.$emit("input", this.address);
					this.loading = false;
				},
			});
		},
	},
	watch: {
		search() {
			this.googleSearch();
		},
		address() {
			if (this.address != null) {
				this.loading = true;
				this.geocode();
			} else {
				this.$emit("input", null);
			}
		},
		value() {
			this.init();
		},
	},
	created() {
		this.init();
	},
};
</script>