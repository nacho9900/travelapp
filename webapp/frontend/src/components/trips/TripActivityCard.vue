<template>
	<v-card>
		<v-app-bar flat color="rgba(0, 0, 0, 0)">
			<v-toolbar-title>{{ name }}</v-toolbar-title>
			<v-spacer></v-spacer>
			<v-btn color="primary" icon>
				<v-icon>mdi-dots-vertical</v-icon>
			</v-btn>
		</v-app-bar>
		<v-card-text class="py-1"
			>{{ $t("components.trips.trip_activity_card.from") + " " }}
			<b>
				{{ startDateFormatted }}
			</b>
			{{ " " + $t("components.trips.trip_activity_card.to") + " " }}
			<b>
				{{ endDateFormatted }}
			</b>
		</v-card-text>
		<v-card-text class="pt-1">{{ address }}</v-card-text>
	</v-card>
</template>

<script>
import { formatDateTimeString } from "../../utils.js";

export default {
	props: {
		id: Number,
		name: String,
		category: String,
		startDate: String,
		endDate: String,
		address: String,
	},
	computed: {
		startDateFormatted() {
			return formatDateTimeString(this.startDate);
		},
		endDateFormatted() {
			return formatDateTimeString(this.endDate);
		},
	},
};
</script>