<template>
	<v-dialog v-model="dialog" :width="width">
		<v-card :loading="loading">
			<v-card-title>{{ title }}</v-card-title>
			<v-card-text>{{ message }}</v-card-text>
			<v-card-actions>
				<v-spacer></v-spacer>
				<v-btn color="error" text @click="remove" :disabled="loading">
					{{ $t("ui.delete_dialog.delete") }}
				</v-btn>
				<v-btn color="primary" text @click="cancel" :disabled="loading"
					>{{ $t("ui.delete_dialog.cancel") }}
				</v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script>
export default {
	props: {
		value: Boolean,
		message: String,
		loading: Boolean,
		title: String,
		item: Object,
		width: { type: String, default: "350" },
	},
	computed: {
		dialog: {
			get() {
				return this.value;
			},
			set(dialog) {
				this.$emit("input", dialog);
			},
		},
	},
	methods: {
		remove() {
			this.$emit("remove");
		},
		cancel() {
			this.$emit("cancel");
		},
	},
};
</script>