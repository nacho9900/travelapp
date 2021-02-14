<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title class="font-weight-regular">{{
				$t("components.user.change_password_form.title")
			}}</v-card-title>
			<v-card-text>
				<v-container fluid>
					<v-col cols="12">
						<password-text-field
							v-model="passwordCurrent"
							:label="
								$t(
									'components.user.change_password_form.current_password'
								)
							"
							:rules="requiredRule"
							:disabled="loading"
							dense
						>
						</password-text-field>
					</v-col>
					<v-col cols="12">
						<password-text-field
							v-model="passwordNew"
							:label="
								$t(
									'components.user.change_password_form.new_password'
								)
							"
							:rules="
								requiredRule
									.concat(passwordRules)
									.concat(repeatPasswordRule)
							"
							:disabled="loading"
							dense
						>
						</password-text-field>
					</v-col>
					<v-col cols="12">
						<password-text-field
							v-model="passwordNewRepeat"
							:label="
								$t(
									'components.user.change_password_form.repeat_password'
								)
							"
							:rules="
								requiredRule
									.concat(passwordRules)
									.concat(repeatPasswordRule)
							"
							:disabled="loading"
							dense
						>
						</password-text-field>
					</v-col>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-container class="pt-0" fluid>
					<v-col cols="6" class="pt-0">
						<v-btn
							color="primary"
							type="submit"
							:disabled="loading"
							>{{
								$t(
									"components.user.change_password_form.change_password"
								)
							}}</v-btn
						>
					</v-col>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
import { requiredRule, passwordRules } from "../../rules.js";

export default {
	props: {
		loading: Boolean,
	},
	data() {
		return {
			requiredRule,
			passwordRules,
			passwordCurrent: "",
			passwordNew: "",
			passwordNewRepeat: "",
			repeatPasswordRule: [
				() =>
					this.passwordNew === this.passwordNewRepeat ||
					this.$t(
						"components.user.change_password_form.repeat_password_rule"
					),
			],
		};
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				this.$emit("submit", {
					passwordNew: this.passwordNew,
					passwordCurrent: this.passwordCurrent,
				});
			}
		},
	},
};
</script>