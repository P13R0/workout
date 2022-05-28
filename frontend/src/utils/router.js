import { createRouter, createWebHistory } from '@ionic/vue-router';
import LoginView from '@/views/LoginView.vue';
import HomeTabs from '@/components/HomeTabs.vue'

const routes = [
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  },
  {
    path: '/login',
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
        component: () => import('@/views/TraineesView.vue'),
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

router.beforeEach((to, from, next) => {
  const publicPages = ['/login'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('token');

  if (authRequired && !loggedIn) {
    return next('/login');
  }

  next();
})


