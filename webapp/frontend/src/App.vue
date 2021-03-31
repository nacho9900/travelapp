<template>
	<v-app id="inspire">
		<v-navigation-drawer v-if="isAuth" v-model="drawer" temporary fixed>
			<v-list nav dense>
				<v-subheader>TravelApp</v-subheader>
				<v-list-item :to="{ name: 'Home' }">
					<v-list-item-icon>
						<v-icon>mdi-home</v-icon>
					</v-list-item-icon>
					<v-list-item-title>{{ $t("app.home") }}</v-list-item-title>
				</v-list-item>
			</v-list>

			<v-list nav dense>
				<v-subheader>{{ $t("app.my_stuff") }}</v-subheader>
				<v-list-item :to="{ name: 'Account' }">
					<v-list-item-icon>
						<v-icon>mdi-account</v-icon>
					</v-list-item-icon>
					<v-list-item-title>{{
						$t("app.account")
					}}</v-list-item-title>
				</v-list-item>
				<v-list-item :to="{ name: 'UserTrips' }">
					<v-list-item-icon
						><v-icon>mdi-earth</v-icon></v-list-item-icon
					>
					<v-list-item-title>
						{{ $t("app.my_trips") }}
					</v-list-item-title>
				</v-list-item>
			</v-list>

			<v-list nav dense>
				<v-subheader>
					{{ $t("app.view_options") }}
				</v-subheader>
				<v-list-item>
					<v-switch
						v-model="$vuetify.theme.dark"
						:label="modeLabel"
						color="primary"
						dense
					></v-switch>
				</v-list-item>
			</v-list>
		</v-navigation-drawer>

		<v-app-bar color="primary" app>
			<v-app-bar-nav-icon
				v-if="isAuth"
				@click="drawer = !drawer"
			></v-app-bar-nav-icon>
			<v-toolbar-title> TravelApp </v-toolbar-title>
			<v-spacer></v-spacer>
			<user-menu
				v-if="isAuth && hasUser"
				:id="user.id"
				:firstname="user.firstname"
				:lastname="user.lastname"
			></user-menu>
		</v-app-bar>

		<v-main>
			<transition name="route" mode="out-in">
				<router-view> </router-view>
			</transition>
		</v-main>

		<v-footer padless>
			<v-card
				flat
				tile
				color="primary"
				class="text-center"
				style="width: 100%"
			>
				<v-card-text>
					{{ $t("app.made_by") }} Ignacio Negro Caino (57509)
				</v-card-text>
				<v-card-text>
					<b>TravelApp — 2021</b>
				</v-card-text>
			</v-card>
		</v-footer>
	</v-app>
</template>

<script>
import UserMenu from "components/user/UserMenu.vue";

export default {
	components: {
		UserMenu,
	},
	data: () => ({
		drawer: false,
	}),
	computed: {
		modeLabel() {
			return this.$vuetify.theme.dark
				? this.$t("app.dark_mode")
				: this.$t("app.light_mode");
		},
		logoPath() {
			return require("@/assets/logos/lb.png");
		},
		autologout() {
			return this.$store.getters.didAutologout;
		},
		isAuth() {
			return this.$store.getters.isAuth;
		},
		hasUser() {
			return this.$store.getters.hasUser;
		},
		user() {
			return this.$store.getters.user;
		},
	},
	created() {
		// if (this.isAuthenticated && !this.hasUser) {
		// 	this.$store.dispatch("loadUser");
		// }
	},
	watch: {
		autologout() {
			if (this.autologout) {
				this.$router.replace({ name: "Login" });
			}
		},
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