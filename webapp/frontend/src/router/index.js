import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store/index.js';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: { name: 'Home' }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import("views/home/Home.vue"),
    meta: { requireAuth: true }
  },
  {
    path: '/trip/:id',
    name: 'Trip',
    component: () => import("views/trips/Trip.vue"),
    meta: { requireAuth: true },
    props: true,
  },
  {
    path: '/trip/new',
    name: 'TripNew',
    component: () => import("views/trips/TripNew.vue"),
    meta: { requireAuth: true }
  },
  {
    path: '/trip',
    name: 'UserTrips',
    component: () => import("views/user/UserTrips.vue"),
    meta: { requireAuth: true }
  },
  {
    path: '/trip-not-found',
    name: 'TripNotFound',
    component: () => import("views/trips/TripNotFound.vue"),
    meta: { requireAuth: true }
  },
  {
    path: '/account',
    name: 'Account',
    component: () => import("views/user/UserAccount.vue"),
    meta: { requireAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("views/authentication/Login.vue"),
    meta: { requireUnauth: true }
  },
  {
    path: '/signin',
    name: 'SignIn',
    component: () => import("views/authentication/SignUp.vue"),
    meta: { requireUnauth: true }
  },
  {
    path: '/recovery',
    name: 'PasswordRecovery',
    component: () => import("views/authentication/PasswordRecovery.vue"),
    meta: { requireUnauth: true }
  },
  {
    path: '/recovery/:token',
    name: 'ChangePassword',
    component: () => import("views/authentication/ChangePassword.vue"),
    meta: { requireUnauth: true },
    props: true,
  },
  {
    path: '/verify/:token',
    name: 'EmailVerification',
    component: () => import("views/authentication/EmailVerification.vue"),
    meta: { requireUnauth: true },
    props: true,
  },
  {
    path: '/connection-lost',
    name: "NetworkError",
    component: () => import("views/error/NetworkError.vue"),
  },
  {
    path: '/:notFound(.*)',
    component: () => import("views/error/PageNotFound.vue"),
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.NODE_ENV === 'production'
    ? '/paw-2019a-4/'
    : '/',
  routes
});

router.beforeEach((to, from, next) => {
  store.dispatch('tryLogin').then(
    () => {
      if (to.meta.requireAuth && !store.getters.isAuth) {
        next('/login');
      } else if (to.meta.requireUnauth && store.getters.isAuth) {
        next('/');
      } else {
        next();
      }
    }
  );
});

export default router;
