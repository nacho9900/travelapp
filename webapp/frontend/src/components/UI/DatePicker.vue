<template>
	<v-menu
		v-model="dateMenu"
		:close-on-content-click="false"
		offset-y
		max-width="290"
	>
		<template v-slot:activator="{ on, attrs }">
			<v-text-field
				prepend-icon="mdi-calendar"
				:label="label"
				v-on="on"
				v-bind="attrs"
				v-model="dateFormatted"
				readonly
				clearable
                :dense="dense"
			></v-text-field>
		</template>
		<template default>
			<v-date-picker
				locale="es-AR"
				v-model="date"
				no-title
			></v-date-picker>
		</template>
	</v-menu>
</template>

<script>
export default {
	props: {
		value: String,
        label: String,
        dense: Boolean,
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
					? new Date(this.date).toLocaleDateString("es-AR", opt)
					: "";
			},
			set(value) {
				if (!value || value === "") {
					this.date = null;
				}
			},
		},
	},
	watch: {
		date() {
			this.$emit("input", this.date);
		},
	},
};
</script>