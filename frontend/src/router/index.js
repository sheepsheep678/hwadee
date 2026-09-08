import { createRouter, createWebHistory } from 'vue-router'

// 医生端
import DoctorLogin from '@/views/doctor/Login.vue'
import DoctorLayout from '@/views/doctor/DoctorLayout.vue'
import DoctorDashboard from '@/views/doctor/Dashboard.vue'
import ElderProfile from '@/views/doctor/ElderProfile.vue'
import Device from '@/views/doctor/Device.vue'
import Profile from '@/views/doctor/Profile.vue'

// 老人端
import ElderLogin from '@/views/elder/Login.vue'
import ElderLayout from '@/views/elder/ElderLayout.vue'
import ElderHome from '@/views/elder/Home.vue'
import ElderSelfProfile from '@/views/elder/Profile.vue'
import ElderDevice from '@/views/elder/Device.vue'
import ElderPersonalCenter from '@/views/elder/PersonalCenter.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    // 默认进入医生登录页
    {
      path: '/',
      redirect: '/doctor/login',
    },

    // =========================
    // 医生端
    // =========================

    {
      path: '/doctor/login',
      name: 'doctor-login',
      component: DoctorLogin,
    },

    {
      path: '/doctor',
      component: DoctorLayout,
      meta: {
        requiresAuth: true,
        userType: 'doctor',
      },

      children: [
        {
          path: '',
          name: 'doctor-dashboard',
          component: DoctorDashboard,
        },

        {
          path: 'elder-profiles',
          name: 'doctor-elder-profiles',
          component: ElderProfile,
        },

        {
          path: 'devices',
          name: 'doctor-devices',
          component: Device,
        },

        {
          path: 'profile',
          name: 'doctor-profile',
          component: Profile,
        },
      ],
    },

    // =========================
    // 老人端
    // =========================

    {
      path: '/elder/login',
      name: 'elder-login',
      component: ElderLogin,
    },

    {
      path: '/elder',
      component: ElderLayout,
      meta: {
        requiresAuth: true,
        userType: 'elder',
      },

      children: [
        {
          path: '',
          name: 'elder-home',
          component: ElderHome,
        },

        {
          path: 'profile',
          name: 'elder-profile',
          component: ElderSelfProfile,
        },

        {
          path: 'devices',
          name: 'elder-devices',
          component: ElderDevice,
        },

        {
          path: 'personal-center',
          name: 'elder-personal-center',
          component: ElderPersonalCenter,
        },
      ],
    },
  ],
})

// 路由守卫
router.beforeEach((to) => {
  if (!to.meta.requiresAuth) {
    return
  }

  // 医生端
  if (to.meta.userType === 'doctor') {
    const doctorToken = localStorage.getItem('accessToken')

    if (!doctorToken) {
      return '/doctor/login'
    }
  }

  // 老人端
  if (to.meta.userType === 'elder') {
    const elderToken = localStorage.getItem('elderAccessToken')

    if (!elderToken) {
      return '/elder/login'
    }
  }
})

export default router
