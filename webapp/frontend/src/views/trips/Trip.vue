<template>
	<v-container fluid>
		<v-row justify="center">
			<v-col cols="12" md="6">
				<v-card>
					<v-tabs
						color="secondary"
						background-color="primary"
						show-arrows
						fixed-tabs
						centered
					>
						<v-tab> {{ $t("views.trip.trip") }}</v-tab>
						<v-tab>{{ $t("views.trip.activities") }}</v-tab>
						<v-tab>{{ $t("views.trip.members") }}</v-tab>
						<v-tabs-slider color="secondary"></v-tabs-slider>
						<v-tab-item>
							<v-container fluid>
								<v-row>
									<v-col cols="12" class="px-0 pt-0">
										<trip-card
											:id="id"
											@notFound="
												$router.replace({
													name: 'TripNotFound',
												})
											"
											:actions="true"
										></trip-card>
									</v-col>
								</v-row>
							</v-container>
						</v-tab-item>
						<v-tab-item>
							<trip-activities :tripId="id"></trip-activities>
						</v-tab-item>
						<v-tab-item>
							<trip-members :tripId="id"></trip-members>
						</v-tab-item>
					</v-tabs>
				</v-card>
			</v-col>
			<v-col cols="12" md="4">
				<v-card>
					<v-tabs
						color="secondary"
						background-color="primary"
						fixed-tabs
						centered
					>
						<v-tab>{{ $t("views.trip.comments") }}</v-tab>
						<v-tab-item>
							<trip-comments :id="id"></trip-comments>
						</v-tab-item>
					</v-tabs>
				</v-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripCard from "components/trips/TripCard.vue";
import TripActivities from "components/trips/TripActivities.vue";
import TripMembers from "components/trips/TripMembers.vue";
import TripComments from "components/trips/TripComments.vue";

export default {
	components: {
		TripCard,
		TripActivities,
		TripMembers,
		TripComments,
	},
	props: {
		id: {
			type: String,
			required: true,
		},
	},
};
</script>