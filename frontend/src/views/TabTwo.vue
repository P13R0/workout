<template>
  <ion-page id="main-content">
    <ion-content>
      <ion-tab-bar tab="tab2">
        <ion-header translucent>
          <ion-toolbar>
            <ion-buttons slot="start">
              <ion-menu-button></ion-menu-button>
            </ion-buttons>
            <ion-title>Tab Two</ion-title>
          </ion-toolbar>
        </ion-header>
      </ion-tab-bar>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar, IonMenuButton, IonButtons } from '@ionic/vue';

export default {
  name: "TabTwo",
  components: { IonTabBar, IonMenuButton, IonButtons }
}
</script>

<style scoped>

</style>
