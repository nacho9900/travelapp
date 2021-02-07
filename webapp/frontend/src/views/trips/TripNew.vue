<template>
	<v-container fluid>
		<v-row justify="center">
			<v-col cols="12" lg="6" md="9">
				<trip-form></trip-form>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import TripForm from "components/trips/TripForm.vue";

export default {
	components: {
		TripForm,
	},
};
</script>