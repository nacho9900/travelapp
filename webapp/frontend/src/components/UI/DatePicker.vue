<template>
	<v-menu
		v-model="dateMenu"
		:close-on-content-click="true"
		offset-y
		max-width="290"
	>
		<template v-slot:activator="{ on, attrs }">
			<v-text-field
				:label="label"
				:rules="rules"
				:dense="dense"
				:outlined="outlined"
				v-on="on"
				v-bind="attrs"
				v-model="dateFormatted"
				readonly
				clearable
			></v-text-field>
		</template>
		<template default>
			<v-date-picker
				v-model="date"
				:locale="locale"
				no-title
			></v-date-picker>
		</template>
	</v-menu>
</template>

<script>
import getBrowserLocale from "../../i18n/get-user-locale";

export default {
	props: {
		value: String,
		label: String,
		dense: Boolean,
		outlined: Boolean,
		rules: Array,
	},
	data() {
		return {
			dateMenu: false,
			date: null,
		};
	},
	computed: {
		dateFormatted: {
			get() {
				const opt = {
					timeZone: "UTC",
				};

				return this.date
					? new Date(this.date).toLocaleDateString(getBrowserLocale(), opt)
					: "";
			},
			set(value) {
				if (!value || value === "") {
					this.date = null;
				}
			},
		},
		locale() {
			return getBrowserLocale();
		},
	},
	watch: {
		date() {
			this.$emit("input", this.date);
		},
	},
};
</script>