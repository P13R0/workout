import { createRouter, createWebHistory } from '@ionic/vue-router';
import LoginView from '@/views/LoginView.vue';
import HomeTabs from '@/components/HomeTabs.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/home/',
    component: HomeTabs,
    children: [
      {
        path: '',
        redirect: '/home/tab1',
      },
      {
        path: 'tab1',
        component: () => import('@/views/TabOne.vue'),
      },
      {
        path: 'tab2',
        component: () => import('@/views/TabTwo.vue'),
      },
      {
        path: 'tab3',
        component: () => import('@/views/TabThree.vue'),
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router


