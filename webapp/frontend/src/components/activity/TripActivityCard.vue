<template>
	<v-card>
		<v-app-bar flat color="rgba(0, 0, 0, 0)">
			<v-toolbar-title>{{ name }}</v-toolbar-title>
			<v-spacer></v-spacer>
			<v-menu bottom right v-if="actions">
				<template v-slot:activator="{ on, attrs }">
					<v-btn color="primary" icon v-bind="attrs" v-on="on">
						<v-icon>mdi-dots-vertical</v-icon>
					</v-btn>
				</template>

				<v-list>
					<v-list-item @click="edit">
						<v-list-item-title>{{
							$t(
								"components.trips.trip_activity_card.edit_activity"
							)
						}}</v-list-item-title>
					</v-list-item>
					<v-list-item @click="remove">
						<v-list-item-title>{{
							$t("components.trips.trip_activity_card.remove")
						}}</v-list-item-title>
					</v-list-item>
				</v-list>
			</v-menu>
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
import { formatDateString } from "../../utils.js";

export default {
	props: {
		id: Number,
		name: String,
		category: String,
		startDate: String,
		endDate: String,
		address: String,
		actions: Boolean,
	},
	computed: {
		startDateFormatted() {
			return formatDateString(this.startDate);
		},
		endDateFormatted() {
			return formatDateString(this.endDate);
		},
	},
	methods: {
		edit() {
			this.$emit("edit", { id: this.id });
		},
		remove() {
			this.$emit("remove", { id: this.id });
		},
	},
};
</script>