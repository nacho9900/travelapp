<template>
	<v-dialog width="350" v-model="dialog" persistent>
		<v-card>
			<v-card-text>
				{{ message }}
				<v-progress-linear
					indeterminate
					size="50"
				></v-progress-linear>
			</v-card-text>
		</v-card>
	</v-dialog>
</template>

<script>
export default {
	props: ["value", "message"],
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