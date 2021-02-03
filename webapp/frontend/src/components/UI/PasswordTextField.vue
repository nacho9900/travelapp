<template>
	<v-text-field
		v-model="password"
		:type="passwordType"
		:label="label"
		:outlined="outlined"
		:dense="dense"
		:append-icon="passwordIcon"
		:rules="rules"
		:disabled="disabled"
		@click:append="toggleInputPasswordType"
	>
	</v-text-field>
</template>

<script>
export default {
	props: {
		dense: Boolean,
		outlined: Boolean,
		disabled: Boolean,
		value: String,
		label: String,
		rules: Array,
	},
	data() {
		return {
			password: "",
			passwordVisivility: false,
		};
	},
	computed: {
		passwordIcon() {
			return this.passwordVisivility ? "mdi-eye-off" : "mdi-eye";
		},
		passwordType() {
			return this.passwordVisivility ? "text" : "password";
		},
	},
	methods: {
		toggleInputPasswordType() {
			this.passwordVisivility = !this.passwordVisivility;
		},
	},
	watch: {
		password() {
			this.$emit("input", this.password);
		},
	},
};
</script>