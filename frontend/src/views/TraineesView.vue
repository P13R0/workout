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
        <ion-fab-button id="add-trainee-button" @click="setOpen(true)"><ion-icon :icon="add"></ion-icon></ion-fab-button>
      </ion-fab>

      <ion-modal :is-open="isOpenRef" canDismiss="true" :presenting-element="$parent.$refs.ionRouterOutlet" @didDismiss="setOpen(false)">
        <AddTraineeModal/>
      </ion-modal>
    </ion-content>

  </ion-page>
</template>


<script>
import { IonTabBar, IonMenuButton, IonButtons, IonFab, IonFabButton, IonModal } from '@ionic/vue';
import { add } from 'ionicons/icons';
import { defineComponent, ref } from 'vue';
import AddTraineeModal from "@/components/AddTraineeModal";

export default defineComponent ({
  name: "TraineesView",
  components: { AddTraineeModal, IonTabBar, IonMenuButton, IonButtons, IonFab, IonFabButton, IonModal },
  setup() {
    const isOpenRef = ref(false)
    const setOpen = (state) => isOpenRef.value = state
    return { isOpenRef, setOpen, add }
  }
})
</script>

<style scoped>

</style>
