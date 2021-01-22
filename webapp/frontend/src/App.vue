<template>
	<v-app id="inspire">
		<v-navigation-drawer v-model="drawer" temporary fixed>
			<v-list>
				<v-list-item>
					<v-list-item-avatar>
						<v-img
							src="https://randomuser.me/api/portraits/men/1.jpg"
						></v-img>
					</v-list-item-avatar>
				</v-list-item>
			</v-list>
			<v-divider></v-divider>
			<v-list>
				<v-list-item>
					<v-switch
						v-model="$vuetify.theme.dark"
						:label="modeLabel"
						color="primary"
					></v-switch>
				</v-list-item>
			</v-list>
			<v-divider></v-divider>
			<v-list nav dense>
				<v-list-item :to="{ name: 'Home' }" link>
					<v-list-item-title>Home</v-list-item-title>
				</v-list-item>
			</v-list>
		</v-navigation-drawer>

		<v-app-bar color="primary" app>
			<v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
			<v-toolbar-title> TravelApp </v-toolbar-title>
			<v-spacer></v-spacer>
		</v-app-bar>

		<v-main>
			<transition name="route" mode="out-in">
				<router-view> </router-view>
			</transition>
		</v-main>
	</v-app>
</template>

<script>
export default {
	data: () => ({
		drawer: false,
	}),
	computed: {
		modeLabel() {
			return this.$vuetify.theme.dark ? "Modo Oscuro" : "Modo Claro";
		},
	},
	methods: {},
	created() {
		// if (this.isAuthenticated && !this.hasUser) {
		// 	this.$store.dispatch("loadUser");
		// }
	},
};
</script>

<style scoped>
.theme--light.v-application {
	background-color: var(--v-background-base, white) !important;
}

.route-enter-from {
	opacity: 0;
	transform: translateY(-30px);
}

.route-leave-to {
	opacity: 0;
	transform: translateY(30px);
}

.route-enter-active {
	transition: all 0.3s ease-out;
}

.route-leave-active {
	transition: all 0.3s ease-in;
}

.route-enter.to,
.route-leave-from {
	opacity: 1;
	transform: translateY(0);
}
</style>