<template>
  <ion-page>
    <ion-content>
      <ion-tab-bar tab="tab3">
        <ion-header translucent>
          <ion-toolbar>
            <ion-title>Tab Three</ion-title>
          </ion-toolbar>
        </ion-header>

        <ion-content fullscreen class="ion-padding">
          <h1>Games</h1>
        </ion-content>
      </ion-tab-bar>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar } from '@ionic/vue';

export default {
  name: "TabThree",
  components: { IonTabBar }
}
</script>

<style scoped>

</style>
