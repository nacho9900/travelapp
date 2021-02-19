<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card :loading="loading">
			<v-card-title class="font-weight-regular">
				{{ $t("components.trips.trip_join_button.join_dialog_title") }}
			</v-card-title>
			<v-card-text class="text--primary">
				{{
					$t("components.trips.trip_join_button.join_dialog_message")
				}}
			</v-card-text>
			<v-card-text class="pb-0">
				<v-textarea
					v-model="message"
					:label="
						$t(
							'components.trips.trip_join_button.join_dialog_message_label'
						)
					"
					:rules="messageSizeRule"
					:disabled="loading"
					rows="3"
					counter="255"
					outlined
				></v-textarea>
			</v-card-text>
			<v-card-actions class="pt-0">
				<v-container fluid>
					<v-row>
						<v-col cols="12" class="pt-0">
							<v-btn
								type="submit"
								color="primary"
								:disabled="loading"
								>{{
									$t(
										"components.trips.trip_join_button.join_dialog_btn"
									)
								}}</v-btn
							>
						</v-col>
					</v-row>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
export default {
	props: {
		loading: Boolean,
	},
	data() {
		return {
			messageSizeRule: [
				(v) =>
					!v ||
					v.length < 255 ||
					this.$t(
						"components.trips.trip_join_form.message_size_rule"
					),
			],
			message: null,
		};
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				this.$emit("submit", { message: this.message });
			}
		},
	},
};
</script>