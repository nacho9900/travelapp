<template>
	<v-container fluid>
		<v-row justify="center">
			<v-col
				cols="12"
				style="padding-top: 100px"
				class="d-flex justify-center"
			>
				<v-img :src="require('@/assets/connection-lost.png')" max-width="200"></v-img>
			</v-col>
			<v-col cols="12" md="6" class="d-flex justify-center py-1">
				<h4 class="font-weight-regular">
					{{ $t("views.error.connection_lost.message") }}
				</h4>
			</v-col>
		</v-row>
	</v-container>
</template>
