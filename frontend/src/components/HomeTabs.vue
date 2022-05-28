<template>
  <ion-page>
    <HomeMenu></HomeMenu>
    <ion-content>
      <ion-tabs>
        <ion-router-outlet></ion-router-outlet>
        <ion-tab-bar slot="bottom">
          <ion-tab-button tab="tab1" href="/home/tab1">
            <ion-icon :icon="people" />
            <ion-label>Trainees</ion-label>
          </ion-tab-button>
          <ion-tab-button tab="tab2" href="/home/tab2">
            <ion-icon :icon="ellipse" />
            <ion-label>Tab 2</ion-label>
          </ion-tab-button>
          <ion-tab-button tab="tab3" href="/home/tab3">
            <ion-icon :icon="square" />
            <ion-label>Tab 3</ion-label>
          </ion-tab-button>
        </ion-tab-bar>
      </ion-tabs>

    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar, IonTabButton, IonTabs, IonRouterOutlet } from '@ionic/vue';
import { ellipse, square, people } from 'ionicons/icons';
import HomeMenu from "@/components/HomeMenu";

export default {
  name: "HomeTabs",
  components: { IonTabs, IonTabBar, IonTabButton, IonRouterOutlet, HomeMenu },
  setup: () => ({ ellipse, square, people })
}
</script>

<style scoped>

</style>
