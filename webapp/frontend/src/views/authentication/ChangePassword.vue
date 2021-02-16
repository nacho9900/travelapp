<template>
	<v-container>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center">
			<v-col cols="12" md="6">
				<change-password-form
					@submit="submit"
					:loading="loading"
					:success="success"
				>
					<template v-slot:success>
						<v-alert type="success" text>
							{{ $t("view.change_password.success") }}
							<router-link :to="{ name: 'Login' }">{{
								$t("view.password_recovery.login")
							}}</router-link>
						</v-alert>
					</template>
				</change-password-form>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import ChangePasswordForm from "components/user/ChangePasswordForm.vue";

export default {
	components: {
		ChangePasswordForm,
	},
	props: {
		token: String,
	},
	data() {
		return {
			loading: false,
			success: false,
			error: null,
		};
	},
	methods: {
		async submit(password) {
			this.loading = true;

			const data = {
				token: this.token,
				password: password.passwordNew,
			};

			try {
				await this.$store.dispatch("changePasswordRecovery", data);
				this.success = true;
			} catch (error) {
				this.error = this.$t(
					"view.change_password.not_found_or_expired"
				);
			}

			this.loading = false;
		},
	},
};
</script>