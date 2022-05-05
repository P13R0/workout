import { createApp } from 'vue'
import {IonCardSubtitle, IonicVue} from '@ionic/vue';
import App from './App.vue';
import router from './router';
import './registerServiceWorker'

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

import { IonPage, IonContent, IonCard, IonCardTitle, IonCardHeader, IonCardContent, IonInput, IonLabel, IonItem, IonButton, IonIcon } from '@ionic/vue'

const app = createApp(App).use(IonicVue).use(router);

app.component('ion-page', IonPage);
app.component('ion-content', IonContent);
app.component('ion-card', IonCard);
app.component('ion-card-title', IonCardTitle);
app.component('ion-card-subtitle', IonCardSubtitle);
app.component('ion-card-header', IonCardHeader);
app.component('ion-card-content', IonCardContent);
app.component('ion-input', IonInput);
app.component('ion-label', IonLabel);
app.component('ion-item', IonItem);
app.component('ion-button', IonButton);
app.component('ion-icon', IonIcon);

router.isReady().then(() => {
  app.mount('#app');
});
