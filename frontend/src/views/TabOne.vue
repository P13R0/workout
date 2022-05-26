<template>
  <ion-page>
    <ion-content>
      <ion-tab-bar tab="tab1">
        <div class="ion-page" id="main-content">
        <ion-header translucent>
          <ion-toolbar>
            <ion-buttons slot="start">
              <ion-menu-button></ion-menu-button>
            </ion-buttons>
            <ion-title>Tab One</ion-title>
          </ion-toolbar>
        </ion-header>
        </div>
      </ion-tab-bar>

      <ion-button shape="round" @click="logout()">Logout</ion-button>

    </ion-content>
  </ion-page>
</template>


<script>
import { IonTabBar, IonMenuButton, IonButtons } from '@ionic/vue';

export default {
  name: "TabOne",
  components: { IonTabBar, IonMenuButton, IonButtons },
  methods: {
    logout() {
      localStorage.clear()
    }
  }
}
</script>

<style scoped>

</style>
