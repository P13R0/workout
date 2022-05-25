<template>
  <ion-page>
    <ion-content>
      <ion-tab-bar tab="tab1">
        <ion-header translucent>
          <ion-toolbar>
            <ion-title>Tab One</ion-title>
          </ion-toolbar>
        </ion-header>
      </ion-tab-bar>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonTabBar } from '@ionic/vue';

export default {
  name: "TabOne",
  components: { IonTabBar }
}
</script>

<style scoped>

</style>
