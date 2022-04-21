import { createRouter, createWebHashHistory } from '@ionic/vue-router';
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView";

const routes = [
  {
    path: '/',
    name: 'login',
    component: LoginView
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
