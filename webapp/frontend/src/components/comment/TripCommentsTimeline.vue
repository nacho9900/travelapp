<template>
	<v-container>
		<v-row>
			<v-col cols="12" v-if="loading">
				<v-skeleton-loader type="sentences" class="py-5 px-5"></v-skeleton-loader>
				<v-skeleton-loader type="sentences" class="py-5 px-5"></v-skeleton-loader>
				<v-skeleton-loader type="sentences" class="py-5 px-5"></v-skeleton-loader>
				<v-skeleton-loader type="sentences" class="py-5 px-5"></v-skeleton-loader>
			</v-col>
			<v-col cols="12" v-else>
				<v-virtual-scroll
					height="400"
					:items="comments"
					item-height="130"
					bench="1"
				>
					<template v-slot:default="{ item }">
						<trip-comment-card
							:id="item.id"
							:comment="item.comment"
							:firstname="item.member.user.firstname"
							:lastname="item.member.user.lastname"
							:createdOn="item.createdOn"
						></trip-comment-card>
					</template>
				</v-virtual-scroll>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripCommentCard from "./TripCommentCard.vue";

export default {
	components: { TripCommentCard },
	props: {
		comments: Array,
		loading: Boolean,
	},
};
</script>