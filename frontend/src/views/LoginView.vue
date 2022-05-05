<template>
  <ion-page>
    <ion-content>
      <div class="center">
        <ion-card>
          <ion-card-header>
            <ion-card-title class="ion-text-center">Workout Manager</ion-card-title>
          </ion-card-header>
          <ion-card-content class="ion-text-center">
            <form @submit.prevent="handleLogin">
              <ion-item>
                <ion-label position="floating">Username</ion-label>
                <ion-input v-model="username" required></ion-input>
              </ion-item>
              <ion-item>
                <ion-label position="floating">Password</ion-label>
                <ion-input v-model="password" type="password" required></ion-input>
              </ion-item>
              <ion-button type="submit" shape="round">
                Login <ion-icon :icon="logInOutline" slot="start">
              </ion-icon></ion-button>
            </form>
          </ion-card-content>
        </ion-card>
      </div>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonPage, IonCard, IonInput, IonLabel, IonItem, IonButton, IonIcon } from '@ionic/vue'
import { logInOutline } from 'ionicons/icons';

export default {
  name: "LoginView",
  components: { IonPage, IonCard, IonInput, IonLabel, IonItem, IonButton, IonIcon },
  setup: () => ({ logInOutline }),
  data: () => ({
    username: "",
    password: ""
  }),
  methods: {}
}
</script>

<style scoped>
ion-content {
  --ion-background-color: #9DD9D2
}
.center{
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
ion-card {
  min-width: 15em;
  --ion-background-color: #FFF8F0
}
ion-button {
  margin-top: 2em;
}
</style>
