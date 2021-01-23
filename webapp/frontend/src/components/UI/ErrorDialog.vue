<template>
	<v-dialog v-model="dialog" max-width="400">
		<v-card>
			<v-card-title>
				{{ $t("ui.error_dialog.error_ocurred") }}
			</v-card-title>
			<v-card-text> {{ error }} </v-card-text>
		</v-card>
	</v-dialog>
</template>

<script>
export default {
	props: ["value", "error"],
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
};
</script>