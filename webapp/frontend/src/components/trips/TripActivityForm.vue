<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title>
				{{ $t("components.trips.trip_activity_form.title") }}
			</v-card-title>
			<v-card-text class="px-0">
				<trip-activities-map
					:activities="activities"
				></trip-activities-map>
			</v-card-text>
			<v-card-text class="pt-1">
				<v-container class="py-0" fluid>
					<v-row>
						<v-col cols="12" class="pa-0">
							<place-autocomplete
								v-model="place"
								:disabled="loading"
								:rules="requiredRule"
							></place-autocomplete>
						</v-col>
						<v-col cols="12" class="pa-0">
							<v-text-field
								v-model="name"
								:disabled="loading"
								:label="
									$t(
										'components.trips.trip_activity_form.name'
									)
								"
								:rules="requiredRule.concat(nameLengthRule)"
								counter="40"
							></v-text-field>
						</v-col>
						<v-col cols="6" class="py-0 pl-0">
							<date-picker
								v-model="startDate"
								:disabled="loading"
								:label="
									$t(
										'components.trips.trip_activity_form.start_date'
									)
								"
								:rules="requiredRule"
							></date-picker>
						</v-col>
						<v-col cols="6" class="py-0 pr-0">
							<date-picker
								v-model="endDate"
								:disabled="loading"
								:label="
									$t(
										'components.trips.trip_activity_form.end_date'
									)
								"
								:rules="requiredRule"
							></date-picker>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-container fluid>
					<v-row justify="end">
						<v-col cols="6" class="px-3 pt-0 d-flex justify-end">
							<v-btn color="primary" type="submit">{{
								$t("components.trips.trip_activity_form.add")
							}}</v-btn>
						</v-col>
					</v-row>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
import TripActivitiesMap from "./TripActivitiesMap.vue";
import { gmapApi } from "gmap-vue";
import { requiredRule } from "../../rules.js";

export default {
	components: { TripActivitiesMap },
	props: {
		loading: Boolean,
	},
	data() {
		return {
			name: "",
			place: null,
			activity: null,
			startDate: null,
			endDate: null,
			requiredRule,
			nameLengthRule: [
				(v) =>
					!v || v.length <= 40 ||
					this.$t(
						"components.trips.trip_activity_form.name_length_rule"
					),
			],
			loadingPlace: false,
			activities: [],
		};
	},
	computed: {
		google: gmapApi,
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				const activity = {
					place: {
						...this.place,
					},
					name: this.name,
					startDate: this.startDate,
					endDate: this.endDate,
				};

				this.$emit("submit", activity);
			}
		},
	},
	watch: {
		place() {
			if (this.place != null) {
				this.activity = { id: 1, place: this.place };
				this.activities = [this.activity];
			}
		},
		loading() {
			if (!this.loading) {
				this.$refs.form.reset();
			}
		},
	},
};
</script>