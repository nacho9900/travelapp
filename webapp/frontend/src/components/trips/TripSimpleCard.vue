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
				<v-card-title class="font-weight-regular"
					><span
						style="
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
						width: 100%
						display: inline-block;
					"
						>{{ name }}</span
					></v-card-title
				>
				<v-card-text class="text--primary pt-0" style="height: 100px">
					<div
						style="
							display: -webkit-box;
							max-width: 100%;
							-webkit-line-clamp: 4;
							-webkit-box-orient: vertical;
							overflow: hidden;
						"
					>
						{{ description }}
					</div>
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
		pictureUrl: String,
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
				? require("@/assets/no-image-available.png")
				: `${this.pictureUrl}?height=200`;
		},
	},
};
</script>