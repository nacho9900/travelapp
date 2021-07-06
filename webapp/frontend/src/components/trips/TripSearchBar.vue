<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card color="primary">
			<v-container fluid>
				<v-row>
					<v-col cols="12" md="4">
						<place-autocomplete
							v-model="locationEntered"
							:label="
								$t('components.trips.trip_search_bar.location')
							"
							hide-details
							solo
						></place-autocomplete>
					</v-col>
					<v-col cols="12" md="3">
						<date-picker
							v-model="fromEntered"
							:label="
								$t('components.trips.trip_search_bar.start')
							"
							:min="minDate"
							hide-details
							solo
						></date-picker>
					</v-col>
					<v-col cols="12" md="3">
						<date-picker
							v-model="toEntered"
							:label="$t('components.trips.trip_search_bar.end')"
							:min="fromEntered || minDate"
							hide-details
							solo
						></date-picker>
					</v-col>
					<v-col cols="12" md="2" class="d-flex justify-end">
						<v-btn
							color="accent"
							class="mr-2"
							:to="{ name: 'TripNew' }"
							fab
						>
							<v-icon>mdi-plus</v-icon>
						</v-btn>
						<v-btn type="submit" color="secondary" fab>
							<v-icon> mdi-magnify </v-icon>
						</v-btn>
					</v-col>
				</v-row>
			</v-container>
		</v-card>
	</v-form>
</template>

<script>
export default {
	props: {
		from: String,
		to: String,
		location: Object,
	},
	data() {
		return {
			fromEntered: null,
			toEntered: null,
			locationEntered: null,
		};
	},
	methods: {
		init() {
			this.fromEntered = this.from;
			this.toEntered = this.to;
			this.locationEntered = this.location ? { ...this.location } : null;
		},
		submit() {
			if (this.$refs.form.validate()) {
				const data = {
					from: this.fromEntered,
					to: this.toEntered,
					location: this.locationEntered,
				};

				this.$emit("submit", data);
			}
		},
	},
	computed: {
		minDate() {
			return new Date().toISOString();
		},
	},
	watch: {
		from() {
			if (this.from) {
				this.fromEntered = this.from;
			}
		},
		to() {
			if (this.to) {
				this.toEntered = this.to;
			}
		},
		location() {
			if (this.location) {
				this.locationEntered = this.location;
			}
		},
	},
	created() {
		this.init();
	},
};
</script>