<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <ion-card>
        <ion-card-header>
          <ion-card-title>Workout Manager</ion-card-title>
        </ion-card-header>
        <ion-card-content>
          <form @submit.prevent="handleLogin">
            <ion-item>
              <ion-label position="floating">Username</ion-label>
              <ion-input v-model="username" required></ion-input>
            </ion-item>
            <ion-item>
              <ion-label position="floating">Password</ion-label>
              <ion-input v-model="password" type="password" required></ion-input>
            </ion-item>
            <ion-button type="submit" shape="round">Login</ion-button>
          </form>
        </ion-card-content>
      </ion-card>
    </ion-content>
  </ion-page>
</template>

<script>
import {IonPage, IonCard, IonInput, IonLabel, IonItem, IonButton} from '@ionic/vue'

export default {
  name: "LoginView",
  components: {IonPage, IonCard, IonInput, IonLabel, IonItem, IonButton},
  data: () => ({
    username: "",
    password: ""
  }),
  methods: {}
}
</script>

<style scoped>
ion-button {
  margin-top: 2em;
}
</style>
