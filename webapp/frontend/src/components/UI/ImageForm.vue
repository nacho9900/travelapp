<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title>{{ $t("ui.image_form.title") }}</v-card-title>
			<v-card-text>
				<v-file-input
					accept="image/*"
					v-model="file"
					:rules="sizeRule"
					:disabled="loading"
				></v-file-input>
			</v-card-text>
			<v-card-actions>
				<v-spacer></v-spacer>
				<v-btn text color="primary" type="submit" :disabled="loading">
					{{ $t("ui.image_form.upload") }}
				</v-btn>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
export default {
	props: {
		loading: Boolean,
	},
	data() {
		return {
			file: null,
			sizeRule: [
				(v) => !!v || this.$t("ui.image_form.image_required"),
				(v) =>
					!v ||
					v.size <= 5000000 ||
					this.$t("ui.image_form.image_size"),
			],
		};
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				const reader = new FileReader();

				reader.addEventListener("load", () => {
					this.$emit("submit", {
						name: this.file.name,
						body: reader.result.split(",")[1],
					});
				});

				reader.readAsDataURL(this.file);
			}
		},
	},
};
</script>