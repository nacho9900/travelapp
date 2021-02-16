<template>
	<v-menu transition="slide-y-transition" bottom>
		<template v-slot:activator="{ on, attrs }">
			<v-avatar color="secondary" v-bind="attrs" v-on="on"> IN </v-avatar>
		</template>

		<v-list>
			<v-list-item @click="signOut">
				<v-list-item-icon>
					<v-icon>mdi-logout</v-icon>
				</v-list-item-icon>
				<v-list-item-title>
					{{ $t("components.user.user_menu.sign_out") }}
				</v-list-item-title>
			</v-list-item>
		</v-list>
	</v-menu>
</template>

<script>
export default {
	methods: {
		signOut() {
			this.$store.dispatch("logout");
			this.$router.replace({ name: "Login" });
		},
	},
};
</script>