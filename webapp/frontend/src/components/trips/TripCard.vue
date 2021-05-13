<template>
	<v-skeleton-loader v-if="loading" type="image, article">
	</v-skeleton-loader>
	<v-hover v-else>
		<template v-slot:default="{ hover }">
			<v-card :elevation="!!to && hover ? 16 : 0" rounded="0" :to="to">
				<simple-error-dialog v-model="error"></simple-error-dialog>
				<v-dialog
					v-model="formDialog"
					:persistent="loadingEdit"
					width="500"
				>
					<trip-form
						:name="trip.name"
						:description="trip.description"
						:startDate="trip.startDate"
						:endDate="trip.endDate"
						:loading="loadingEdit"
						edit
						@submit="update"
					></trip-form>
				</v-dialog>
				<v-dialog
					v-model="imageFormDialog"
					:persistent="loadingImage"
					width="400"
				>
					<image-form
						@submit="uploadImage"
						:loading="loadingImage"
					></image-form>
				</v-dialog>
				<v-img
					class="white--text align-end"
					height="200px"
					:src="imageUrl"
					:contain="imageError"
					@error="imageError = true"
				>
				</v-img>
				<v-app-bar color="primary" flat>
					<v-toolbar-title>{{ trip.name }}</v-toolbar-title>
					<v-spacer></v-spacer>

					<v-menu bottom right v-if="showActions">
						<template v-slot:activator="{ on, attrs }">
							<v-btn icon v-bind="attrs" v-on="on">
								<v-icon>mdi-dots-vertical</v-icon>
							</v-btn>
						</template>

						<v-list>
							<v-list-item @click="formDialog = true">
								<v-list-item-title>{{
									$t("components.trips.trip_card.edit")
								}}</v-list-item-title>
							</v-list-item>
							<v-list-item @click="imageFormDialog = true">
								<v-list-item-title>
									{{
										$t(
											"components.trips.trip_card.change_image"
										)
									}}
								</v-list-item-title>
							</v-list-item>
						</v-list>
					</v-menu>
				</v-app-bar>
				<v-card-text class="text--primary pt-2" style="height: 100px">
					<div>{{ trip.description }}</div>
				</v-card-text>
				<v-card-text class="text--primary">
					{{ $t("components.trips.trip_card.from") }}
					{{ formatDateString(trip.startDate) }}
					{{ $t("components.trips.trip_card.to") }}
					{{ formatDateString(trip.endDate) }}
				</v-card-text>
				<v-card-actions v-if="showJoinButton" class="pt-0 px-4">
					<trip-join-button
						:id="trip.id"
						:member="member"
						:request="request"
						:joinUrl="trip.tripJoinRequestUri"
						@joined="joined"
						@exit="exit"
					></trip-join-button>
				</v-card-actions>
			</v-card>
		</template>
	</v-hover>
</template>

<script>
import TripForm from "components/trips/TripForm.vue";
import TripJoinButton from "components/request/TripJoinButton.vue";
import getBrowserLocale from "../../i18n/get-user-locale";
import { memberRoles } from "../../enums";

export default {
	components: { TripForm, TripJoinButton },
	props: {
		trip: Object,
		member: Object,
		request: Object,
		actions: Boolean,
		to: Object,
		loading: Boolean,
	},
	data() {
		return {
			error: null,
			formDialog: false,
			loadingEdit: false,
			imageFormDialog: false,
			loadingImage: false,
			imageError: false,
			cacheBreaker: null,
		};
	},
	computed: {
		imageUrl() {
			return this.imageError
				? require("@/assets/no-image-available.png")
				: this.trip.tripPictureUri +
						(this.cacheBreaker ? `&${this.cacheBreaker}` : "");
		},
		isMember() {
			return !!this.role;
		},
		showJoinButton() {
			return !this.isMember || this.role.canExitTrip;
		},
		showActions() {
			return this.isMember && this.actions && this.role.canEditTrip;
		},
		role() {
			return !this.member
				? null
				: memberRoles.find((x) => x.value === this.member.role);
		},
	},
	methods: {
		formatDateString(date) {
			const opt = {
				timeZone: "UTC",
			};

			return date
				? new Date(date).toLocaleDateString(getBrowserLocale(), opt)
				: "";
		},
		async update(tripUpdates) {
			this.loadingEdit = true;

			try {
				this.trip = await this.$store.dispatch("trip/update", {
					id: this.id,
					...tripUpdates,
				});
			} catch (error) {
				this.error = this.$t("components.trips.trip_card.error_update");
			}

			this.loadingEdit = false;
		},
		async uploadImage(image) {
			this.loadingImage = true;

			try {
				await this.$store.dispatch("trip/uploadImage", {
					image: image,
					id: this.id,
				});
				this.imageFormDialog = false;
				this.imageError = false;
				this.cacheBreaker = new Date().getTime();
			} catch (error) {
				this.error = this.$t(
					"components.trips.trip_card.change_image_error"
				);
			}

			this.loadingImage = false;
		},
		joined(request) {
			this.$emit("joined", request);
		},
		exit() {
			this.role = null;
			this.$emit("exit");
		},
	},
};
</script>