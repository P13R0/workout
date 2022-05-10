import { createApp } from 'vue'
import { IonicVue } from '@ionic/vue';
import App from './App.vue';
import router from './utils/router';
import './utils/registerServiceWorker'
import axios from 'axios'

/* Core CSS required for Ionic components to work properly */
import '@ionic/vue/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/vue/css/normalize.css';
import '@ionic/vue/css/structure.css';
import '@ionic/vue/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/vue/css/padding.css';
import '@ionic/vue/css/float-elements.css';
import '@ionic/vue/css/text-alignment.css';
import '@ionic/vue/css/text-transformation.css';
import '@ionic/vue/css/flex-utils.css';
import '@ionic/vue/css/display.css';

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

axios.defaults.baseURL = 'https://workout-manager-back-end.herokuapp.com'
axios.defaults.headers.Accept = 'application/json'

axios.interceptors.request.use(config => {
  if (localStorage.getItem('token') != null) {
    config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`;
  }
  return config
}, error => {
  console.log(error);
  return Promise.reject(error)
});

router.isReady().then(() => {
  app.mount('#app');
});
