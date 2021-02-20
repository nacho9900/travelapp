<template>
	<v-card>
		<v-app-bar flat color="rgba(0, 0, 0, 0)">
			<v-toolbar-title class="font-weight-regular">
				<user-avatar
					:color="roleObj.color"
					:id="userId"
					:firstname="firstname"
					:lastname="lastname"
				>
				</user-avatar>

				{{ fullname }}</v-toolbar-title
			>
			<v-spacer></v-spacer>
			<v-chip class="black--text" :color="roleObj.color">{{
				roleObj.text
			}}</v-chip>
			<v-menu v-if="roleObj.canBeEdited && actions" bottom right>
				<template v-slot:activator="{ on, attrs }">
					<v-btn color="primary" icon v-bind="attrs" v-on="on">
						<v-icon>mdi-dots-vertical</v-icon>
					</v-btn>
				</template>

				<v-list>
					<v-list-item @click="deleteMember">
						<v-list-item-icon>
							<v-icon> mdi-logout </v-icon>
						</v-list-item-icon>
						<v-list-item-title>{{
							$t("components.trips.trip_activity_card.remove")
						}}</v-list-item-title>
					</v-list-item>
				</v-list>
			</v-menu>
		</v-app-bar>
		<v-card-text class="text--primary pt-0" v-if="biography">
			{{ biography }}
		</v-card-text>
	</v-card>
</template>

<script>
import UserAvatar from "components/user/UserAvatar.vue";
import { memberRoles } from "../../enums.js";

export default {
	props: {
		id: Number,
		userId: Number,
		firstname: String,
		lastname: String,
		biography: String,
		role: String,
		actions: Boolean,
	},
	components: {
		UserAvatar,
	},
	data() {
		return {
			roleObj: memberRoles.find((x) => x.value === this.role),
		};
	},
	computed: {
		fullname() {
			return this.firstname + " " + this.lastname;
		},
	},
	methods: {
		deleteMember() {
			this.$emit("delete", { id: this.id });
		},
	},
};
</script>