<template>
	<v-form @submit.prevent="submit" ref="form">
		<v-card elevation="0">
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
								rows="3"
								dense
								outlined
								hide-details
							></v-textarea>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>
			<v-card-actions class="py-1">
				<v-container fluid>
					<v-row justify="end">
						<v-col cols="12" class="d-flex justify-end pa-0">
							<v-btn type="submit" icon
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
	data() {
		return {
			requiredRule,
			comment: "",
		};
	},
	methods: {
		submit() {
			if (this.$refs.form.validate()) {
				const comment = {
					comment: this.comment,
				};

				this.$emit("submit", comment);
			}
		},
	},
};
</script>