<template>
	<v-card class="mx-2 mt-1">
		<v-card-text class="text--primary py-0 px-1"
			>{{ fullname }}:</v-card-text
		>
		<v-card-text class="text--primary py-1" style="min-height: 72px">{{ comment }}</v-card-text>
		<v-card-text class="d-flex justify-end py-0 px-1">
			{{ createOnFormatted }}
		</v-card-text>
	</v-card>
</template>

<script>
import { formatDateTimeString } from "../../utils.js";

export default {
	props: {
		id: Number,
		comment: String,
		firstname: String,
		lastname: String,
		createdOn: String,
	},
	computed: {
		fullname() {
			return this.firstname + " " + this.lastname;
		},
		createOnFormatted() {
			return formatDateTimeString(this.createdOn);
		},
	},
};
</script>