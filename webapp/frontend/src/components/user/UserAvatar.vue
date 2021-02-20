<template>
	<v-avatar :rounded="rounded" :tile="tile" :color="color">
		<span v-if="!url || url === ''">{{ initials }}</span>
		<v-img :src="url" @error="url = ''" v-else></v-img>
	</v-avatar>
</template>

<script>
export default {
	props: {
		id: Number,
		firstname: String,
		lastname: String,
		color: String,
		value: Object,
		tile: Boolean,
		rounded: Boolean,
	},
	data() {
		return {
			url: "",
		};
	},
	computed: {
		initials() {
			return this.firstname
				? this.firstname.split(" ")[0][0] +
						this.lastname.split(" ")[0][0]
				: "";
		},
	},
	methods: {
		init() {
			this.url =
				process.env.VUE_APP_API_BASE_URL +
				`/users/${this.id}/picture?width=50`;
		},
	},
	created() {
		this.init();
	},
};
</script>