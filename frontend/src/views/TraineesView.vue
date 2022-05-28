<template>
  <ion-page id="main-content">
    <ion-content>
      <ion-tab-bar tab="tab1">
        <ion-header translucent>
          <ion-toolbar>
            <ion-buttons slot="start">
              <ion-menu-button></ion-menu-button>
            </ion-buttons>
            <ion-title>Trainees</ion-title>
          </ion-toolbar>
        </ion-header>
      </ion-tab-bar>
    </ion-content>
  </ion-page>
</template>


<script>
import { IonTabBar, IonMenuButton, IonButtons } from '@ionic/vue';

export default {
  name: "TraineesView",
  components: { IonTabBar, IonMenuButton, IonButtons }
}
</script>

<style scoped>

</style>
