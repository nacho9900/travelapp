<template>
	<v-card elevation="0" rounded="0">
		<v-img
			class="white--text align-end"
			height="200px"
			src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
		>
			<v-app-bar flat color="rgba(0, 0, 0, 0)">
				<v-toolbar-title class="white--text">{{
					name
				}}</v-toolbar-title>
				<v-spacer></v-spacer>
				<v-btn color="white" icon>
					<v-icon>mdi-dots-vertical</v-icon>
				</v-btn>
			</v-app-bar>
		</v-img>
		<v-card-text class="text--primary">
			<div>{{ description }}</div>
		</v-card-text>
		<v-card-text class="text--primary">
			Desde {{ formatDateString(startDate) }} hasta el
			{{ formatDateString(endDate) }}
		</v-card-text>
	</v-card>
</template>

<script>
import getBrowserLocale from "../../i18n/get-user-locale";

export default {
	props: {
		name: String,
		description: String,
		startDate: String,
		endDate: String,
	},
	methods: {
		formatDateString(date) {
			const opt = {
				timeZone: "UTC",
			};

			return date
				? new Date(date).toLocaleDateString(getBrowserLocale(), opt)
				: "";
		},
	},
};
</script>