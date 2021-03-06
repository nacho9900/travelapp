<template>
	<v-container>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center">
			<v-col cols="12" md="6" class="d-flex justify-center">
				<v-form @submit.prevent="login" ref="form">
					<v-card
						elevation="0"
						outlined
						width="400"
						:loading="loading"
					>
						<v-card-title>
							<v-container>
								<v-row justify="center">
									<v-col
										cols="12"
										class="px-0 pb-4 d-flex justify-center"
									>
										<v-img
											:src="logoUrl"
											alt="Logo"
											max-width="200"
										></v-img>
									</v-col>
								</v-row>
								<v-row justify="center">
									{{ $t("views.login.title") }}
								</v-row>
							</v-container>
						</v-card-title>
						<v-card-subtitle>
							<v-row justify="center">
								{{ $t("views.login.subtitle") }}
							</v-row>
						</v-card-subtitle>
						<v-card-text>
							<v-container>
								<v-row>
									<v-col cols="12">
										<v-text-field
											:label="
												$t('views.login.email_label')
											"
											v-model="email"
											:disabled="loading"
											:rules="
												requiredRule.concat([
													() =>
														!unauthorized ||
														$t(
															'views.login.incorrect_email_password'
														),
												]).concat(notVerifiedRule)
											"
											outlined
										></v-text-field>
									</v-col>
									<v-col cols="12">
										<v-text-field
											:label="
												$t('views.login.password_label')
											"
											v-model="password"
											:disabled="loading"
											:rules="
												requiredRule.concat([
													() => !unauthorized || '',
												])
											"
											type="password"
											outlined
										></v-text-field>
										<v-btn
											link
											:to="{ name: 'PasswordRecovery' }"
											text
											color="primary"
											small
											:disabled="loading"
										>
											{{
												$t(
													"views.login.password_forget"
												)
											}}</v-btn
										>
									</v-col>
								</v-row>
							</v-container>
						</v-card-text>
						<v-card-actions>
							<v-container>
								<v-row justify="space-between">
									<v-btn
										color="primary"
										text
										:disabled="loading"
										:to="{ name: 'SignIn' }"
										link
										>{{
											$t("views.login.signin_btn")
										}}</v-btn
									>
									<v-btn
										elevation="0"
										color="primary"
										:disabled="loading"
										type="submit"
										>{{
											$t("views.login.login_btn")
										}}</v-btn
									>
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
import { requiredRule } from "../../rules.js";

export default {
	data() {
		return {
			loading: false,
			email: "",
			password: "",
			error: null,
			unauthorized: false,
			requiredRule,
			notVerified: false,
			notVerifiedRule: [
				() => !this.notVerified || this.$t("views.login.not_verified"),
			],
		};
	},
	computed: {
		logoUrl() {
			return require("@/assets/logos/green_w_logo.png");
		},
	},
	methods: {
		async login() {
			this.unauthorized = false;

			if (this.$refs.form.validate()) {
				this.loading = true;
				try {
					await this.$store.dispatch("login", {
						email: this.email,
						password: this.password,
					});

					await this.$router.replace({ name: "Home" });
				} catch (err) {
					if (err?.response?.status === 401) {
						this.unauthorized = true;
						this.$refs.form.validate();
					} else if (err?.response?.status === 403) {
						this.notVerified = true;
						this.$refs.form.validate();
					} else {
						this.error = this.$t("views.login.error");
					}
				}
				this.loading = false;
			}
		},
	},
};
</script>