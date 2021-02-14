<template>
	<v-container fluid>
		<v-row justify="center">
			<v-col
				cols="12"
				style="padding-top: 100px"
				class="d-flex justify-center"
			>
				<v-img src="logos/green_w_logo.png" max-width="200"></v-img>
			</v-col>
			<v-col cols="12" class="d-flex justify-center py-1">
				<h2 class="font-weight-regular">
					{{ $t("views.error.page_not_found.message") }}
				</h2>
			</v-col>
			<v-col cols="12" class="d-flex justify-center py-1">
				<router-link :to="{ name: 'Home' }">
					<h3 class="font-weight-regular">
						{{ $t("views.error.page_not_found.redirect") }}
					</h3>
				</router-link>
			</v-col>
		</v-row>
	</v-container>
</template>