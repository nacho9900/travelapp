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
								:rules="requiredRule"
								:disabled="loading"
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
										.concat(futureDateRule)
										.concat(datesRule)
								"
								:disabled="loading"
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
										.concat(futureDateRule)
										.concat(datesRule)
								"
								:disabled="loading"
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
								:rules="requiredRule"
								:disabled="loading"
							>
							</v-textarea>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-container fluid>
					<v-row justify="end">
						<v-col cols="6" class="d-flex justify-end">
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
import { requiredRule, futureDateRule } from "../../rules.js";

export default {
	props: {
		name: String,
		description: String,
		startDate: String,
		endDate: String,
		loading: Boolean,
		edit: Boolean,
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
			futureDateRule,
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