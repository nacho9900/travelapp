<template>
	<v-container fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center">
			<v-col cols="12" md="6" class="pt-10">
				<v-form @submit.prevent="submit" ref="form">
					<v-card :loading="loading">
						<v-card-title class="font-weight-regular">
							{{ $t("view.password_recovery.title") }}
						</v-card-title>
						<v-card-subtitle class="text--primary pt-3">
							{{ $t("view.password_recovery.subtitle") }}
						</v-card-subtitle>
						<v-card-text class="pb-0">
							<v-text-field
								v-model="email"
								:label="$t('view.password_recovery.email')"
								:rules="requiredRule.concat(emailRules)"
								:disabled="loading || success"
								outlined
								validate-on-blur
							></v-text-field>
						</v-card-text>
						<v-card-subtitle
							v-if="success"
							class="text--primary pt-0"
						>
							<v-alert type="success" text>
								{{ $t("view.password_recovery.success") }}
                                <router-link :to="{name: 'Login'}">{{ $t("view.password_recovery.login")}}</router-link>
							</v-alert>
						</v-card-subtitle>
						<v-card-actions class="pt-0">
							<v-container class="pt-0" fluid>
								<v-row>
									<v-col>
										<v-btn
											color="primary"
											:disabled="loading || success"
											type="submit"
											>{{
												$t(
													"view.password_recovery.continue"
												)
											}}</v-btn
										>
									</v-col>
								</v-row>
							</v-container>
						</v-card-actions>
					</v-card>
				</v-form>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import { requiredRule, emailRules } from "../../rules.js";

export default {
	data() {
		return {
			success: false,
			error: null,
			loading: false,
			requiredRule,
			emailRules,
			email: "",
		};
	},
	methods: {
		async submit() {
			this.loading = true;

			if (this.$refs.form.validate()) {
				try {
					await this.$store.dispatch("passwordRecovery", {
						email: this.email,
					});
					this.success = true;
				} catch (error) {
					this.error = this.$t("view.password_recovery.error");
				}
			}

			this.loading = false;
		},
	},
};
</script>