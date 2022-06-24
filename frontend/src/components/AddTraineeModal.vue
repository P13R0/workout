<template>
  <ion-modal ref="modal" trigger="add-trainee-button" @willDismiss="onWillDismiss">
    <ion-header>
      <ion-toolbar>
        <ion-buttons slot="start">
          <ion-button @click="cancel()">Cancel</ion-button>
        </ion-buttons>
        <ion-title>{{ title }}</ion-title>
        <ion-buttons slot="end">
          <ion-button :strong="true" @click="add()">Add</ion-button>
        </ion-buttons>
      </ion-toolbar>
    </ion-header>
    <ion-content class="ion-padding">
      <ion-item>
        <ion-label position="floating">Name</ion-label>
        <ion-input ref="input" v-model="traineeName" required></ion-input>
      </ion-item>
      <ion-item>
        <ion-label position="floating">Email</ion-label>
        <ion-input ref="input" v-model="traineeEmail" type="email" required></ion-input>
      </ion-item>
    </ion-content>
  </ion-modal>
</template>

<script>
import { IonButtons, IonModal} from '@ionic/vue';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: "AddTraineeModal",
  components: { IonButtons, IonModal },
  data: () => ({
    title: "New Trainee",
    traineeName: null,
    traineeEmail: null
  }),
  setup() {
    const isOpenRef = ref(false)
    const setOpen = (state) => isOpenRef.value = state
    return { isOpenRef, setOpen }
  },
  methods: {
    cancel() {
      this.$refs.modal.$el.dismiss(null, 'cancel');
    },
    confirm() {
      const name = this.$refs.input.$el.value;
      this.$refs.modal.$el.dismiss(name, 'confirm');
    },
    onWillDismiss(ev) {
      if (ev.detail.role === 'confirm') {
        console.log("ADD NEW TRAINEE")
      }
    }
  }
})
</script>

<style scoped>

</style>
