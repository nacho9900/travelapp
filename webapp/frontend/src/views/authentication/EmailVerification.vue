<template>
	<v-container fluid>
		<v-row justify="center">
			<v-col cols="12" class="d-flex justify-center mt-10">
				<v-img src="/logos/green_w_logo.png" max-width="200"></v-img>
			</v-col>
		</v-row>
		<v-row v-if="loading" justify="center" class="pt-5">
			<v-col cols="12" class="d-flex justify-center">
				<h3 class="font-weight-regular">
					{{ $t("views.auth.email_verification.loading_title") }}
				</h3>
			</v-col>
			<v-col cols="12" class="d-flex justify-center pt-10">
				<v-progress-circular
					color="primary"
					size="100"
					width="7"
					indeterminate
				></v-progress-circular>
			</v-col>
		</v-row>
		<v-row v-else-if="error">
			<v-col cols="12" class="d-flex justify-center">
				<h3 class="font-weight-regular">
					{{ $t("views.auth.email_verification.error_title") }}
				</h3>
			</v-col>
		</v-row>
		<v-row v-else justify="center" class="pt-5">
			<v-col cols="12" class="d-flex justify-center">
				<h3 class="font-weight-regular">
					{{ $t("views.auth.email_verification.success_title") }}
				</h3>
			</v-col>
			<v-col cols="12" class="d-flex justify-center">
				<v-btn color="primary" :to="{ name: 'Login' }">
					{{ $t("views.auth.email_verification.success_btn") }}
				</v-btn>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
export default {
	props: {
		token: {
			required: true,
			type: String,
		},
	},
	data() {
		return {
			loading: false,
			error: false,
		};
	},
	methods: {
		async verify() {
			this.loading = true;

			try {
				await this.$store.dispatch("verify", {
					token: this.token,
				});
			} catch (error) {
				this.error = true;
			}

			this.loading = false;
		},
	},
	created() {
		this.verify();
	},
};
</script>