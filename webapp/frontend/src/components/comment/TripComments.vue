<template>
	<v-container fluid class="pb-0">
		<v-row v-if="canComment">
			<v-col cols="12" class="pt-5 pb-0">
				<trip-new-comment-box
					:url="url"
					@created="addComment"
				></trip-new-comment-box>
			</v-col>
		</v-row>
		<v-row v-if="canViewComments">
			<v-col cols="12" class="px-0">
				<trip-comments-timeline
					:comments="comments"
					:loading="loading"
				></trip-comments-timeline>
			</v-col>
		</v-row>
		<v-row v-if="!canViewComments">
			<v-col cols="12">
				<v-alert type="info">
					{{ $t("components.trips.trip_commments.unauthorized") }}
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
		url: String,
		role: Object,
	},
	data() {
		return {
			comments: [],
			loading: false,
			error: null,
		};
	},
	computed: {
		canComment() {
			return this.role?.canComment;
		},
		canViewComments() {
			return this.role?.canViewComments;
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
					url: this.url,
				});
				this.comments = data.comments;
			} catch (error) {
				if (error?.response?.status !== 401) {
					this.error = this.$t(
						"components.trips.trip_commments.get_error"
					);
				}
			}

			this.loading = false;
		},
		exit() {
			this.getAll();
		},
	},
	components: {
		TripCommentsTimeline,
		TripNewCommentBox,
	},
	created() {
		if (this.canViewComments) {
			this.getAll();
		}
	},
};
</script>