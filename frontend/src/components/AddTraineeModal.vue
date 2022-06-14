<template>
  <ion-header>
    <ion-toolbar>
      <ion-title>{{ title }}</ion-title>
    </ion-toolbar>
  </ion-header>
  <ion-content class="ion-padding">
    {{ content }}
  </ion-content>
</template>

<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: "AddTraineeModal",
  data: () => ({
    title: "New Trainee",
    content: "Content"
  })
})
</script>

<style scoped>

</style>
