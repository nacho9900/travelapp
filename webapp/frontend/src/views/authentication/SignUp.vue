<template>
	<v-container fluid>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-dialog width="400" persistent v-model="success">
			<v-card>
				<v-card-title
					>{{
						$t("views.signup.welcome") + " " + firstname
					}}!</v-card-title
				>
				<v-card-text
					>{{ $t("views.signup.welcome_text") }}
				</v-card-text>
				<v-card-actions class="d-flex justify-end">
					<v-btn @click="redirectLogin" color="primary">
						{{ $t("views.signup_welcome_continue") }}
					</v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>
		<v-row justify="center">
			<v-col cols="12" md="9" lg="6">
				<sign-up-form
					:loading="loading"
					@submit="signup"
				></sign-up-form>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import SignUpForm from "components/auth/SignUpForm.vue";

export default {
	components: {
		SignUpForm,
	},
	data() {
		return {
			loading: false,
			success: false,
			error: null,
			firstname: "",
		};
	},
	methods: {
		async signup(user) {
			this.loading = true;

			try {
				await this.$store.dispatch("signup", user);
				this.firstname = user.firstname;
				this.success = true;
			} catch (error) {
				if (error.response && error.response.status) {
					const status = error.response.status;
					switch (status) {
						case 409:
							this.error = this.$t("views.signup.errors.422");
							break;
						default:
							this.error = this.$t("views.signup.errors.default");
					}
				} else {
					this.error = this.$t("views.signup.errors.default");
				}
			}

			this.loading = false;
		},
		redirectLogin() {
			this.$router.replace("/login");
		},
	},
};
</script>