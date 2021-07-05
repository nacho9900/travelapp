<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title>
				{{ title }}
			</v-card-title>
			<v-card-text>
				<v-container fluid>
					<v-row>
						<v-col cols="12">
							<v-text-field
								v-model.trim="nameEntered"
								:label="$t('components.trips.tripform.name')"
								:rules="requiredRule.concat(tripNameSizeRule)"
								:disabled="loading"
								outlined
							>
							</v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<date-picker
								v-model="startDateEntered"
								:label="
									$t('components.trips.tripform.start_date')
								"
								:rules="
									requiredRule
										.concat(futureStartDateRule)
										.concat(datesRule)
								"
								:min="today"
								:disabled="loading"
								outlined
							></date-picker>
						</v-col>
						<v-col cols="12" md="6">
							<date-picker
								v-model="endDateEntered"
								:label="
									$t('components.trips.tripform.end_date')
								"
								:rules="
									requiredRule
										.concat(futureEndDateRule)
										.concat(datesRule)
								"
								:min="startDateEntered || today"
								:current="startDateEntered || today"
								:disabled="loading"
								outlined
							></date-picker>
						</v-col>
						<v-col cols="12">
							<v-textarea
								v-model.trim="descriptionEntered"
								:label="
									$t('components.trips.tripform.description')
								"
								:hint="
									$t(
										'components.trips.tripform.description_hint'
									)
								"
								:rules="
									requiredRule.concat(descriptionSizeRule)
								"
								:disabled="loading"
								counter="500"
								outlined
							>
							</v-textarea>
						</v-col>
						<v-col cols="12" class="py-1" v-if="activity-warning">
							<v-alert type="warning" text>
								{{
									$t(										"components.trips.tripform.activity_warning"
									)
								}}
							</v-alert>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-container fluid>
					<v-row>
						<v-col cols="6" class="px-5">
							<v-btn
								color="primary"
								type="submit"
								:disabled="loading"
							>
								{{ buttonText }}
							</v-btn>
						</v-col>
					</v-row>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
import { requiredRule } from "../../rules.js";
import { dateIsFuture } from "../../utils.js";

export default {
	props: {
		name: String,
		description: String,
		startDate: String,
		endDate: String,
		loading: Boolean,
		edit: Boolean,
		activityWarning: Boolean,
	},
	data() {
		return {
			nameEntered: "",
			startDateEntered: null,
			endDateEntered: null,
			descriptionEntered: "",
			datesRule: [
				() =>
					!this.startDateEntered ||
					!this.endDateEntered ||
					new Date(this.startDateEntered).getTime() <=
						new Date(this.endDateEntered).getTime() ||
					this.$t("components.trips.tripform.date_rule"),
			],
			requiredRule,
			futureEndDateRule: [
				() =>
					dateIsFuture(this.endDateEntered) ||
					this.$t("rules.future_date_rule"),
			],
			futureStartDateRule: [
				() =>
					dateIsFuture(this.startDateEntered) ||
					this.$t("rules.future_date_rule"),
			],
			tripNameSizeRule: [
				(v) =>
					!v ||
					v.length <= 100 ||
					this.$t("components.trips.tripform.name_length_rule"),
			],
			descriptionSizeRule: [
				(v) =>
					!v ||
					v.length <= 500 ||
					this.$t("components.trips.tripform.desc_length_rule"),
			],
		};
	},
	computed: {
		title() {
			return this.edit
				? this.$t("components.trips.tripform.title_edit")
				: this.$t("components.trips.tripform.title");
		},
		buttonText() {
			return this.edit
				? this.$t("components.trips.tripform.edit")
				: this.$t("components.trips.tripform.create");
		},
		today() {
			return new Date().toISOString();
		},
	},
	methods: {
		init() {
			if (this.edit) {
				this.nameEntered = this.name;
				this.descriptionEntered = this.description;
				this.startDateEntered = this.startDate;
				this.endDateEntered = this.endDate;
			}
		},
		submit() {
			if (this.$refs.form.validate()) {
				const trip = {
					name: this.nameEntered,
					description: this.descriptionEntered,
					startDate: this.startDateEntered,
					endDate: this.endDateEntered,
				};

				this.$emit("submit", trip);
			}
		},
	},
	created() {
		this.init();
	},
};
</script>