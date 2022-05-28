<template>
  <ion-menu side="start" content-id="main-content">
    <ion-header>
      <ion-toolbar translucent>
        <ion-title>Menu</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content>
      <ion-list>
        <ion-item @click="logout">
          <ion-icon :icon="logOut" slot="start"></ion-icon>
          <ion-label>Logout</ion-label>
        </ion-item>
      </ion-list>
    </ion-content>
  </ion-menu>
</template>

<script>
import { IonMenu, IonList } from '@ionic/vue';
import { defineComponent } from 'vue';
import { logOut } from "ionicons/icons";

export default defineComponent({
  name: "HomeMenu",
  setup: () => ({ logOut }),
  components: { IonMenu, IonList },
  methods: {
    logout() {
      localStorage.clear()
      this.$router.go('/login');
    }
  }
})
</script>

<style scoped>

</style>
