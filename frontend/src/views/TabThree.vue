<template>
  <ion-page id="main-content">
    <ion-tab-bar tab="tab3">
      <ion-header translucent>
        <ion-toolbar>
          <ion-buttons slot="start">
            <ion-menu-button></ion-menu-button>
          </ion-buttons>
          <ion-title>Tab Three</ion-title>
        </ion-toolbar>
      </ion-header>
    </ion-tab-bar>
  </ion-page>
</template>

<script>
import { IonTabBar, IonMenuButton, IonButtons } from '@ionic/vue';

export default {
  name: "TabThree",
  components: { IonTabBar, IonMenuButton, IonButtons }
}
</script>

<style scoped>

</style>
