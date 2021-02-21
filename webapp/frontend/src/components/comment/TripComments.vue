<template>
	<v-container  fluid class="pb-0">
		<v-row v-if="!unauthorized">
			<v-col cols="12" class="pt-5 pb-0">
				<trip-new-comment-box
					:tripId="id"
					@created="addComment"
				></trip-new-comment-box>
			</v-col>
		</v-row>
		<v-row v-if="!unauthorized">
			<v-col cols="12" class="px-0">
				<trip-comments-timeline
					:comments="comments"
					:loading="loading"
				></trip-comments-timeline>
			</v-col>
		</v-row>
		<v-row v-if="unauthorized">
			<v-col cols="12">
				<v-alert type="info">
						{{ $t("components.trips.trip_commments.unauthorized")}}
					</v-alert>	
			</v-col>	
		</v-row>
	</v-container>
</template>

<script>
import TripCommentsTimeline from "components/comment/TripCommentsTimeline.vue";
import TripNewCommentBox from "components/comment/TripNewCommentBox.vue";

export default {
	props: {
		id: {
			required: true,
			type: String,
		},
	},
	data() {
		return {
			comments: [],
			loading: false,
			error: null,
			member: null,
			unauthorized: false,
		};
	},
	computed: {
		hasMember() {
			return !!this.member;
		},
	},
	methods: {
		addComment(comment) {
			this.comments.unshift(comment);
		},
		async getAll() {
			this.loading = true;

			try {
				const data = await this.$store.dispatch("comment/getAll", {
					tripId: this.id,
				});
				this.comments = data.comments;
				this.member = data.member;
			} catch (error) {
				if (error?.response?.status !== 401) {
					this.error = this.$t(
						"components.trips.trip_commments.get_error"
					);
				} else {
					this.unauthorized = true;
				}
			}

			this.loading = false;
		},
		exit() {
			this.getAll();
		}
	},
	components: {
		TripCommentsTimeline,
		TripNewCommentBox,
	},
	created() {
		this.getAll();
	},
};
</script>