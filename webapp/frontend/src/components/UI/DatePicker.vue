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
				:hide-details="hideDetails"
				:solo="solo"
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
				:min="min"
				:max="max"
				no-title
			></v-date-picker>
		</template>
	</v-menu>
</template>

<script>
import getBrowserLocale from "../../i18n/get-user-locale";
import { formatDateString } from "../../utils.js";

export default {
	props: {
		value: String,
		label: String,
		dense: Boolean,
		outlined: Boolean,
		rules: Array,
		disabled: Boolean,
		solo: Boolean,
		hideDetails: Boolean,
		min: String,
		max: String,
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
				return this.date ? formatDateString(this.date) : "";
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
	methods: {
		init() {
			if (this.value) {
				this.date = this.value;
			}
		},
	},
	watch: {
		date() {
			this.dateMenu = false;
			this.$emit("input", this.date);
		},
		value() {
			this.init();
		},
	},
	created() {
		this.init();
	},
};
</script>