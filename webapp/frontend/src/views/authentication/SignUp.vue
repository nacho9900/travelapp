<template>
	<v-container fluid>
		<v-row justify="center">
			<v-col cols="12" md="9" lg="6">
				<sign-up-form></sign-up-form>
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
};
</script>