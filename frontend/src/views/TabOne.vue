<template>
  <ion-page>
    <ion-content>
      <ion-tab-bar tab="tab1">
        <ion-header translucent>
          <ion-toolbar>
            <ion-title>Tab One</ion-title>
          </ion-toolbar>
        </ion-header>
      </ion-tab-bar>

      <ion-button shape="round" @click="logout()">Logout</ion-button>

    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar } from '@ionic/vue';

export default {
  name: "TabOne",
  components: { IonTabBar },
  methods: {
    logout() {
      localStorage.clear()
    }
  }
}
</script>

<style scoped>

</style>
