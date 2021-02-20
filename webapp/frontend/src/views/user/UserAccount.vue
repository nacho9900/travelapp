<template>
	<v-container>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-row justify="center" class="pt-10">
			<v-col cols="12">
				<h2 class="font-weight-regular">
					{{ $t("views.user_account.title") }}
				</h2>
			</v-col>
		</v-row>
		<v-row justify="center">
			<v-col cols="12" md="6" class="pt-5">
				<v-skeleton-loader
					v-if="!hasUser"
					type="card"
				></v-skeleton-loader>
				<user-form
					:id="user.id"
					:firstname="user.firstname"
					:lastname="user.lastname"
					:biography="user.biography"
					:email="user.email"
					:birthday="user.birthday"
					:nationality="user.nationality"
					:loading="loadingUpdate"
					@submit="updateUser"
					v-else
				></user-form>
			</v-col>
			<v-col cols="12" md="6" class="pt-5">
				<change-password-form
					:loading="loadingChangePassword"
					@submit="changePassword"
					current
				></change-password-form>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import UserForm from "components/user/UserForm.vue";
import ChangePasswordForm from "components/user/ChangePasswordForm.vue";

export default {
	components: {
		UserForm,
		ChangePasswordForm,
	},
	data() {
		return {
			loadingUpdate: false,
			loadingChangePassword: false,
			error: null,
		};
	},
	computed: {
		user() {
			return this.$store.getters.user;
		},
		hasUser() {
			return this.$store.getters.hasUser;
		},
	},
	methods: {
		async updateUser(data) {
			this.loadingUpdate = true;

			try {
				await this.$store.dispatch("updateUser", data);
			} catch (error) {
				this.error = this.$t("views.user_account.error_update");
			}

			this.loadingUpdate = false;
		},
		async changePassword(data) {
			this.loadingChangePassword = true;

			try {
				await this.$store.dispatch("changePassword", data);
			} catch (error) {
				if (error?.response?.status === 409) {
					this.error = this.$t(
						"views.user_account.error_passwords_match"
					);
				} else {
					this.error = this.$t(
						"views.user_account.error_change_password"
					);
				}
			}

			this.loadingChangePassword = false;
		},
	},
	created() {
		if (!this.hasUser) {
			this.$store.dispatch("loadUser");
		}
	},
};
</script>