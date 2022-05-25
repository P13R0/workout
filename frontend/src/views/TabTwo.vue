<template>
  <ion-page>
    <ion-content>
      <ion-tab-bar tab="tab2">
        <ion-header translucent>
          <ion-toolbar>
            <ion-title>Tab Two</ion-title>
          </ion-toolbar>
        </ion-header>

        <ion-content fullscreen class="ion-padding">
          <h1>Movies</h1>
        </ion-content>
      </ion-tab-bar>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar } from '@ionic/vue';

export default {
  name: "TabTwo",
  components: { IonTabBar }
}
</script>

<style scoped>

</style>
