<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card tile elevation="0">
			<v-card-text class="px-2 py-1">
				<v-container fluid>
					<v-row>
						<v-col cols="12" class="pa-0">
							<v-textarea
								v-model.trim="comment"
								:rules="requiredRule"
								:label="
									$t(
										'components.trips.trip_new_comment_box.new_comment'
									)
								"
								:disabled="loading"
								:loading="loading"
								counter="160"
								rows="3"
								dense
								outlined
							></v-textarea>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions class="py-1">
				<v-container fluid>
					<v-row justify="end">
						<v-col cols="12" class="d-flex justify-end pa-0">
							<v-btn :disabled="loading" type="submit" icon
								><v-icon color="primary"
									>mdi-send</v-icon
								></v-btn
							>
						</v-col>
					</v-row>
				</v-container>
			</v-card-actions>
		</v-card>
	</v-form>
</template>

<script>
import { requiredRule } from "../../rules.js";

export default {
	props: {
		tripId: {
			required: true,
			type: String,
		},
	},
	data() {
		return {
			requiredRule,
			comment: "",
			loading: false,
		};
	},
	methods: {
		async submit() {
			if (this.$refs.form.validate()) {
				this.loading = true;

				try {
					const data = {
						tripId: this.tripId,
						comment: this.comment,
					};

					const newComment = await this.$store.dispatch(
						"comment/create",
						data
					);
					this.$emit("created", newComment);
				} catch (error) {
					//
				}

				this.$refs.form.reset();
				this.loading = false;
			}
		},
	},
};
</script>