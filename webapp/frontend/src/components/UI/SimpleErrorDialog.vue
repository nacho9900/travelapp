<template>
	<v-dialog v-model="dialog" max-width="400" persistent>
		<v-card>
			<v-card-title>
				{{ $t("ui.error_dialog.error_ocurred") }}
			</v-card-title>
			<v-card-text> {{ value }} </v-card-text>
			<v-card-actions class="pb-3">
				<v-btn @click="dialog = false" color="error"> OK </v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script>
export default {
	props: {
		value: String,
	},
	data() {
		return {
			dialog: !!this.value,
		};
	},
	watch: {
		dialog() {
			if (!this.dialog) {
				this.$emit("input", null);
			}
		},
		value() {
			this.dialog = !!this.value;
		},
	},
};
</script>