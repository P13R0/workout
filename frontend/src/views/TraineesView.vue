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
      <AddTraineeModal/>

      <ion-fab vertical="bottom" horizontal="end" slot="fixed">
        <ion-fab-button id="add-trainee-button"><ion-icon :icon="add"></ion-icon></ion-fab-button>
      </ion-fab>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar, IonMenuButton, IonButtons, IonFab, IonFabButton} from '@ionic/vue';
import AddTraineeModal from "@/components/AddTraineeModal";
import { add } from 'ionicons/icons';
import { defineComponent } from 'vue';

export default defineComponent ({
  name: "TraineesView",
  components: { AddTraineeModal, IonTabBar, IonMenuButton, IonButtons, IonFab, IonFabButton },
  setup: () => ({ add })
})
</script>

<style scoped>

</style>
