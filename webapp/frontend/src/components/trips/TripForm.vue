<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title>
				{{ $t("components.trips.tripform.title") }}
			</v-card-title>
			<v-card-text>
				<v-container fluid>
					<v-row>
						<v-col cols="12">
							<v-text-field
								v-model="name"
								:label="$t('components.trips.tripform.name')"
								:rules="requiredRule"
								:disabled="loading"
							>
							</v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<date-picker
								v-model="startDate"
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
								v-model="endDate"
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
								v-model="description"
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
								{{ $t("components.trips.tripform.create") }}
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
		loading: Boolean,
	},
	data() {
		return {
			name: "",
			startDate: null,
			endDate: null,
			description: "",
			datesRule: [
				() =>
					!this.startDate ||
					!this.endDate ||
					new Date(this.startDate).getTime() <=
						new Date(this.endDate).getTime() ||
					this.$t("components.trips.tripform.date_rule"),
			],
			requiredRule,
			futureDateRule,
		};
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				const trip = {
					name: this.name,
					description: this.description,
					startDate: this.startDate,
					endDate: this.endDate,
				};

				this.$emit("submit", trip);
			}
		},
	},
};
</script>