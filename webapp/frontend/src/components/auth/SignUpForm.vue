<template>
	<v-form @submit.prevent="submit">
		<v-card :loading="loading">
			<v-card-title>{{ $t("views.signup.title") }}</v-card-title>
			<v-card-text>
				<v-container fluid>
					<v-row>
						<v-col cols="12" md="6">
							<v-text-field
								v-model="firstname"
								:label="$t('views.signup.firstname')"
								:rules="requiredRule"
								outlined
								dense
							></v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<v-text-field
								v-model="lastname"
								:label="$t('views.signup.lastname')"
								:rules="requiredRule"
								outlined
								dense
							></v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<date-picker
								v-model="birthdate"
								:label="$t('views.signup.birthday')"
								:rules="requiredRule"
								outlined
								dense
							></date-picker>
						</v-col>
						<v-col cols="12" md="6">
							<v-select
								v-model="nationality"
								:label="$t('views.signup.nationality')"
								:items="countries"
								:rules="requiredRule"
								item-text="countryName"
								value="countryShortCode"
								return-object
								outlined
								dense
							></v-select>
						</v-col>
						<v-col cols="12">
							<v-text-field
								v-model="email"
								:label="$t('views.signup.email')"
								:rules="requiredRule.concat(emailRules)"
								outlined
								dense
							></v-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<password-text-field
								v-model="password"
								:label="$t('views.signup.password')"
								:rules="
									requiredRule
										.concat(passwordRules)
										.concat(repeatPasswordRule)
								"
								dense
								outlined
							></password-text-field>
						</v-col>
						<v-col cols="12" md="6">
							<password-text-field
								v-model="passwordRepeat"
								:label="$t('views.signup.password_repeat')"
								:rules="
									requiredRule
										.concat(passwordRules)
										.concat(repeatPasswordRule)
								"
								dense
								outlined
							></password-text-field>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-container fluid>
					<v-row>
						<v-col cols="12" md="6">
							<v-btn color="primary" :to="{ name: 'Login' }" text>
								{{ $t("views.signup.login_btn") }}
							</v-btn>
						</v-col>
						<v-col cols="12" md="6" class="d-flex justify-end">
							<v-btn color="primary" type="submit">
								{{ $t("views.signup.signin_btn") }}
							</v-btn>
						</v-col>
					</v-row>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
import countries from "country-region-data";
import { emailRules, requiredRule, passwordRules } from "../../rules.js";

export default {
	data() {
		return {
			loading: false,
			countries,
			emailRules,
			requiredRule,
			passwordRules,
			firstname: "",
			lastname: "",
			birthdate: null,
			nationality: countries.find((x) => x.countryShortCode === "AR"),
			email: "",
			password: "",
			passwordRepeat: "",
			repeatPasswordRule: [
				() =>
					this.password === this.repeatPasswordRule ||
					this.$t("views.signup.password_repeat_rule"),
			],
		};
	},
	methods: {
		submit() {},
	},
	computed: {
		imageUrl() {
			return "logos/green_w_logo.png";
		},
	},
};
</script>