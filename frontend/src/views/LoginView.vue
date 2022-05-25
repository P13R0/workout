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
              <ion-button type="submit" shape="round" @click="login()">Login
                <ion-icon :icon="logInOutline" slot="start"></ion-icon>
              </ion-button>
            </form>
          </ion-card-content>
        </ion-card>
      </div>
    </ion-content>
  </ion-page>
</template>

<script>
import UserApi from "@/api/UserApi";
import { defineComponent } from "vue";
import { toastController } from '@ionic/vue';
import { logInOutline, alertCircle } from 'ionicons/icons';

export default defineComponent({
  name: "LoginView",
  setup: () => ({ logInOutline }),
  data: () => ({
    username: "",
    password: ""
  }),
  beforeMount() {
    this.home()
  },
  methods: {
    login() {
      if (localStorage.getItem('token') == null) {
        UserApi.login(this.username, this.password)
          .then(result => {
            localStorage.setItem('token', result.data.token);
            this.home()
          })
          .catch((err) => this.errorToast(err.response.status + " : " + err.response.statusText))
      }
    },
    home() {
      if (localStorage.getItem('token') != null) this.$router.push('/home')
    },
    async errorToast(message) {
      const toast = await toastController
        .create({
          header: 'Error',
          message: message,
          icon: alertCircle,
          position: 'top',
          color: 'danger',
          duration: 3000
        })

      await toast.present();
    },
  }
})
</script>

<style scoped>
ion-content {
  --ion-background-color: #9DD9D2
}

.center {
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
