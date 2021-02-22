<template>
	<v-hover>
		<template v-slot:default="{ hover }">
			<v-card :elevation="hover ? 16 : 0" rounded="0" :to="url">
				<v-img
					:src="imageUrl"
					height="200px"
					@error="imageError = true"
				>
				</v-img>
				<v-card-title class="font-weight-regular">{{
					name
				}}</v-card-title>
				<v-card-text class="text--primary pt-0" style="height: 100px">
					<div>{{ description }}</div>
				</v-card-text>
				<v-card-text class="text--primary">
					{{ $t("components.trips.trip_card.from") }}
					{{ fromFormatted }}
					{{ $t("components.trips.trip_card.to") }}
					{{ toFormatted }}
				</v-card-text>
			</v-card>
		</template>
	</v-hover>
</template>

<script>
import { formatDateString } from "../../utils.js";

export default {
	props: {
		id: Number,
		name: String,
		description: String,
		from: String,
		to: String,
	},
	data() {
		return {
			imageError: false,
		};
	},
	computed: {
		fromFormatted() {
			return formatDateString(this.from);
		},
		toFormatted() {
			return formatDateString(this.to);
		},
		url() {
			return {
				name: "Trip",
				params: { id: this.id.toString() },
			};
		},
		imageUrl() {
			return this.imageError
				? "/no-image-available.png"
				: process.env.VUE_APP_API_BASE_URL +
						`/trip/${this.id}/picture?height=200`;
		},
	},
};
</script>