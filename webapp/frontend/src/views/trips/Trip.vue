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
						<v-tab>Datos del Viaje</v-tab>
						<v-tab>Actividades</v-tab>
						<v-tab>Miembros</v-tab>
						<v-tabs-slider color="secondary"></v-tabs-slider>
						<v-tab-item>
							<v-container fluid>
								<v-row>
									<v-col cols="12" class="px-0 pt-0">
										<trip-card
											:name="trip.name"
											:description="trip.description"
											:startDate="trip.startDate"
											:endDate="trip.endDate"
										></trip-card>
									</v-col>
								</v-row>
							</v-container>
						</v-tab-item>
						<v-tab-item>
							<trip-activities
								:activities="trip.activities"
							></trip-activities>
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
						<v-tab>Comentarios</v-tab>
						<v-tab-item>
							<v-card-text class="px-2 pb-2">
								<v-container fluid class="pb-0">
									<v-row>
										<v-col cols="12" class="px-0">
											<trip-comments-timeline>
											</trip-comments-timeline>
										</v-col>
									</v-row>
									<v-row>
										<v-col cols="12" class="px-0">
											<trip-new-comment-box></trip-new-comment-box>
										</v-col>
									</v-row>
								</v-container>
							</v-card-text>
						</v-tab-item>
					</v-tabs>
				</v-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripCard from "components/trips/TripCard.vue";
import TripCommentsTimeline from "components/trips/TripCommentsTimeline.vue";
import TripNewCommentBox from "components/trips/TripNewCommentBox.vue";
import TripActivities from "components/trips/TripActivities.vue";

export default {
	components: {
		TripCard,
		TripCommentsTimeline,
		TripNewCommentBox,
		TripActivities,
	},
	props: {
		id: String,
	},
	data() {
		return {
			trip: {
				name: "Viaje a la playa",
				description:
					"Nos vamos a la playa todo el fin de semana. vamos a hacer fogones y mirar las estrellas, viaje tranqui, nada loco. El que quiere se suma!!",
				startDate: "2021-3-20",
				endDate: "2021-3-30",
				activities: [
					{
						id: 1,
						name: "Caminata por la playa",
						category: "Caminata",
						startDate: "2021-3-21",
						endDate: "2021-3-22",
						place: {
							latitude: -37.32873353039819,
							longitude: -57.01845585732288,
							address: "Mar de las Pampas, Buenos Aires Province",
						},
					},
					{
						id: 2,
						name: "Cena en restaurant",
						category: "Cena",
						startDate: "2021-3-22",
						endDate: "2021-3-22",
						place: {
							latitude: -37.3268539346949,
							longitude: -57.02356216854637,
							address:
								"Av. El Lucero 518, Mar de las Pampas, Provincia de Buenos Aires",
						},
					},
				],
			},
		};
	},
};
</script>