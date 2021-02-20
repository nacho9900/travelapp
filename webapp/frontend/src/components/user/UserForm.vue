<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title class="font-weight-regular">{{
				$t("components.user.user_form.title")
			}}</v-card-title>
			<v-card-text>
				<v-container fluid>
					<v-row>
						<v-col cols="12" md="6">
							<v-text-field
								v-model="firstnameEntered"
								:label="
									$t('components.user.user_form.firstname')
								"
								:rules="requiredRule"
								:disabled="loading"
								dense
							></v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<v-text-field
								v-model="lastnameEntered"
								:label="
									$t('components.user.user_form.lastname')
								"
								:rules="requiredRule"
								:disabled="loading"
								dense
							></v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<date-picker
								v-model="birthdayEntered"
								:label="
									$t('components.user.user_form.birthday')
								"
								:rules="requiredRule"
								:disabled="loading"
								dense
							></date-picker>
						</v-col>
						<v-col cols="12" md="6">
							<v-select
								v-model="nationalitySelected"
								:label="
									$t('components.user.user_form.nationality')
								"
								:items="countries"
								:rules="requiredRule"
								:disabled="loading"
								item-text="countryName"
								value="countryShortCode"
								return-object
								dense
							></v-select>
						</v-col>
						<v-col cols="12">
							<v-textarea
								v-model="biographyEntered"
								:label="
									$t('components.user.user_form.biography')
								"
								:disabled="loading"
								:rules="biographySizeRule"
								counter="500"
								dense
							>
							</v-textarea>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-container class="pt-0" fluid>
					<v-col cols="6" class="pt-0">
						<v-btn color="primary" type="submit" :disabled="loading">{{
							$t("components.user.user_form.save")
						}}</v-btn>
					</v-col>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
import countries from "country-region-data";
import { requiredRule } from "../../rules.js";

export default {
	props: {
		id: Number,
		email: String,
		firstname: String,
		lastname: String,
		birthday: String,
		nationality: String,
		biography: String,
		loading: Boolean,
	},
	data() {
		return {
			countries,
			requiredRule,
			firstnameEntered: "",
			lastnameEntered: "",
			birthdayEntered: "",
			nationalitySelected: null,
			biographyEntered: "",
			biographySizeRule: [
				(v) =>
					!v ||
					v.length < 500 ||
					this.$t("components.user.user_form.biography_size_rule"),
			],
		};
	},
	methods: {
		init() {
			this.firstnameEntered = this.firstname;
			this.lastnameEntered = this.lastname;
			this.birthdayEntered = this.birthday;
			this.biographyEntered = this.biography;
			this.nationalitySelected = countries.find(
				(x) => x.countryShortCode === this.nationality
			);
		},
		submit() {
			if (this.$refs.form.validate()) {
				const data = {
					id: this.id,
					firstname: this.firstnameEntered,
					lastname: this.lastnameEntered,
					birthday: this.birthdayEntered,
					nationality: this.nationalitySelected.countryShortCode,
					biography: this.biographyEntered,
				};

				this.$emit("submit", data);
			}
		},
	},
	created() {
		this.init();
	},
};
</script>