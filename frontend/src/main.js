import { createApp } from 'vue'
import { IonicVue } from '@ionic/vue';
import App from './App.vue';
import router from './router';
import './registerServiceWorker'
import '@ionic/core/css/ionic.bundle.css'

import { IonPage, IonHeader, IonContent } from '@ionic/vue'
import { IonCard, IonCardTitle, IonCardHeader, IonCardContent } from '@ionic/vue'
import { IonInput, IonLabel, IonItem, IonButton, IonIcon } from '@ionic/vue'

const app = createApp(App)
  .use(IonicVue, { mode: 'ios' })
  .use(router)
  .component('ion-page', IonPage)
  .component('ion-header', IonHeader)
  .component('ion-content', IonContent)
  .component('ion-card', IonCard)
  .component('ion-card-title', IonCardTitle)
  .component('ion-card-header', IonCardHeader)
  .component('ion-card-content', IonCardContent)
  .component('ion-input', IonInput)
  .component('ion-label', IonLabel)
  .component('ion-item', IonItem)
  .component('ion-button', IonButton)
  .component('ion-icon', IonIcon);

router.isReady().then(() => {
  app.mount('#app');
});
