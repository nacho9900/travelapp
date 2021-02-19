<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card color="primary">
			<v-container fluid>
				<v-row>
					<v-col cols="12" md="4">
						<place-autocomplete
							v-model="location"
							:label="
								$t('components.trips.trip_search_bar.location')
							"
							hide-details
							solo
						></place-autocomplete>
					</v-col>
					<v-col cols="12" md="3">
						<date-picker
							v-model="from"
							:label="
								$t('components.trips.trip_search_bar.start')
							"
							hide-details
							solo
						></date-picker>
					</v-col>
					<v-col cols="12" md="3">
						<date-picker
							v-model="to"
							:label="$t('components.trips.trip_search_bar.end')"
							hide-details
							solo
						></date-picker>
					</v-col>
					<v-col cols="12" md="2" class="d-flex justify-end">
						<v-btn color="accent" class="mr-2" :to="{ name: 'TripNew' }" fab>
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
	data() {
		return {
			from: null,
			to: null,
			location: null,
		};
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				const data = {
					from: this.from,
					to: this.to,
					location: this.location,
				};

				this.$emit("submit", data);
			}
		},
	},
};
</script>