import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import registerView from '../views/registerView.vue'
import loginView from '../views/loginView.vue'
import registerKakaoView from '@/views/registerKakaoView.vue'
import registerInfoView from '@/views/registerNormalView.vue'
import RegistserRedirect from '@/redirect/RegistserRedirect.vue'
import LoginRedirect from '@/redirect/LoginRedirect.vue'
import Myhouse from '@/views/Myhouse.vue'
import ExchangeRate from '@/views/ExchangeRate.vue'
import TravelHouse from '@/views/TravelHouse.vue'
import MyPage from '@/views/MyPage.vue'

// test
import TravelNextDest from '@/components/myHouse/TravelNextDest.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/register',
      name: 'register',
      component: registerView
    },
    {
      path: '/login',
      name: 'login',
      component: loginView
    },
    {
      path: '/register-info-kakao',
      name: 'register-info-kakao',
      component: registerKakaoView
    },
    {
      path: '/register-info',
      name: 'register-info',
      component: registerInfoView
    },
    {
      path: '/register-redirect',
      name: 'register-redirect',
      component: RegistserRedirect
    },
    {
      path: '/login-redirect',
      name: 'login-redirect',
      component: LoginRedirect
    },
    {
      path: '/my-house',
      name: 'myhouse',
      component: Myhouse
    },
    {
      path: '/exchange-rate-house',
      name: 'exchange-rate',
      component: ExchangeRate
    },
    {
      path: '/travel-house',
      name: 'travel-house',
      component: TravelHouse
    },
    {
      path: '/travel-next-dest',
      name: 'travel-next-dest',
      component: TravelNextDest
    },
    {
      path: '/my-page',
      name: 'my-page',
      component: MyPage
    }
  ]
})

export default router
