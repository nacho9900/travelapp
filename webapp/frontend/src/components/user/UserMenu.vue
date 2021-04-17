<template>
	<div>
		<v-dialog v-model="changeAvatarDialog" width="400">
			<image-form
				:loading="loadingAvatar"
				@submit="changeAvatar"
			></image-form>
		</v-dialog>
		<simple-error-dialog v-model="error"></simple-error-dialog>
		<v-menu transition="slide-y-transition" bottom offset-y rounded>
			<template v-slot:activator="{ on, attrs }">
				<v-btn v-bind="attrs" v-on="on" icon>
					<user-avatar
						:id="id"
						:firstname="firstname"
						:lastname="lastname"
						color="secondary"
						ref="avatar"
					>
					</user-avatar>
				</v-btn>
			</template>

			<v-list>
				<v-list-item @click="changeAvatarDialog = true">
					<v-list-item-icon>
						<v-icon> mdi-file-image </v-icon>
					</v-list-item-icon>
					<v-list-item-title>
						{{ $t("components.user.user_menu.change_avatar") }}
					</v-list-item-title>
				</v-list-item>
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
	</div>
</template>

<script>
import UserAvatar from "components/user/UserAvatar.vue";

export default {
	props: {
		id: Number,
		firstname: String,
		lastname: String,
	},
	data() {
		return {
			changeAvatarDialog: false,
			loadingAvatar: false,
			error: null,
		};
	},
	components: {
		UserAvatar,
	},
	methods: {
		signOut() {
			this.$store.dispatch("logout");
			this.$router.replace({ name: "Login" });
		},
		async changeAvatar(image) {
			this.loadingAvatar = true;

			try {
				await this.$store.dispatch("changeAvatar", { image: image });
				this.avatarCacheBreaker = new Date().getTime();

				if (this.$refs.avatar) {
					this.$refs.avatar.init();
				}
			} catch (error) {
        console.log(error)
				this.error = this.$t(
					"components.user.user_menu.change_avatar_error"
				);
			}

			this.loadingAvatar = false;
		},
	},
};
</script>