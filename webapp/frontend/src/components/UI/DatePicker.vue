<template>
	<v-menu
		v-model="dateMenu"
		:close-on-content-click="false"
		:disabled="disabled"
		offset-y
		max-width="290"
	>
		<template v-slot:activator="{ on, attrs }">
			<v-text-field
				v-model="dateFormatted"
				:label="label"
				:rules="rules"
				:dense="dense"
				:outlined="outlined"
				:disabled="disabled"
				v-on="on"
				v-bind="attrs"
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
		disabled: Boolean,
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
					? new Date(this.date).toLocaleDateString(
							getBrowserLocale(),
							opt			  )
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
			this.dateMenu = false;
			this.$emit("input", this.date);
		},
	},
};
</script>