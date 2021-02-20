<template>
	<v-avatar :rounded="rounded" :tile="tile" :color="color">
		<span v-if="imageError">{{ initials }}</span>
		<v-img :src="url" @error="imageError = true" v-else></v-img>
	</v-avatar>
</template>

<script>
export default {
	props: {
		id: Number,
		cacheBreaker: Number,
		firstname: String,
		lastname: String,
		color: String,
		value: Object,
		tile: Boolean,
		rounded: Boolean,
	},
	data() {
		return {
			imageError: false,
		};
	},
	computed: {
		initials() {
			return this.firstname
				? this.firstname.split(" ")[0][0] +
						this.lastname.split(" ")[0][0]
				: "";
		},
		url() {
			return (
				process.env.VUE_APP_API_BASE_URL +
				`/users/${this.id}/picture?width=50&${this.cacheBreaker}`
			);
		},
	},
	watch: {
		cacheBreaker() {
			this.imageError = false;
		},
	},
};
</script>