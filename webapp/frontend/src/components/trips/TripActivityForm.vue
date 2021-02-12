<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title>
				{{ title }}
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
								v-model="placeSelected"
								:disabled="loading"
								:rules="requiredRule"
							></place-autocomplete>
						</v-col>
						<v-col cols="12" class="pa-0">
							<v-text-field
								v-model="nameEntered"
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
								v-model="startDateEntered"
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
								v-model="endDateEntered"
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
								buttonText
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
		name: String,
		place: Object,
		startDate: String,
		endDate: String,
		loading: Boolean,
		edit: Boolean,
	},
	data() {
		return {
			nameEntered: "",
			placeSelected: null,
			startDateEntered: null,
			endDateEntered: null,
			requiredRule,
			nameLengthRule: [
				(v) =>
					!v ||
					v.length <= 40 ||
					this.$t(
						"components.trips.trip_activity_form.name_length_rule"
					),
			],
		};
	},
	computed: {
		google: gmapApi,
		activities() {
			if (this.placeSelected) {
				const activity = { id: 1, place: this.placeSelected };
				return [activity];
			} else {
				return [];
			}
		},
		buttonText() {
			return this.edit
				? this.$t("components.trips.trip_activity_form.update")
				: this.$t("components.trips.trip_activity_form.add");
		},
		title() {
			return this.edit
				? this.$t("components.trips.trip_activity_form.title_edit")
				: this.$t("components.trips.trip_activity_form.title");
		},
	},
	methods: {
		init() {
			if (this.edit) {
				this.nameEntered = this.name;
				this.startDateEntered = this.startDate;
				this.endDateEntered = this.endDate;
				this.placeSelected = this.place;
			}
		},
		submit() {
			if (this.$refs.form.validate()) {
				const activity = {
					place: {
						...this.placeSelected,
					},
					name: this.nameEntered,
					startDate: this.startDateEntered,
					endDate: this.endDateEntered,
				};

				this.$emit("submit", activity);
			}
		},
	},
	watch: {
		loading() {
			if (!this.loading) {
				this.$refs.form.reset();
			}
		},
		edit() {
			if (this.edit) {
				this.init();
			}
		},
	},
	created() {
		console.log(this.edit);
		this.init();
	},
};
</script>