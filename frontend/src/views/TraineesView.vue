<template>
  <ion-page id="main-content">

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

    <ion-content>
      <ion-fab vertical="bottom" horizontal="end" slot="fixed">
        <ion-fab-button><ion-icon :icon="add"></ion-icon></ion-fab-button>
      </ion-fab>
    </ion-content>

  </ion-page>
</template>


<script>
import { IonTabBar, IonMenuButton, IonButtons, IonFab, IonFabButton } from '@ionic/vue';
import { add } from 'ionicons/icons';

export default {
  name: "TraineesView",
  setup: () => ({ add }),
  components: { IonTabBar, IonMenuButton, IonButtons, IonFab, IonFabButton }
}
</script>

<style scoped>

</style>
