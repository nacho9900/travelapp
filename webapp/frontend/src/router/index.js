import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store/index.js';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import("views/home/Home.vue"),
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
    component: () => import("views/authentication/SignIn.vue"),
    meta: { requireUnauth: true }
  },
  {
    path: '/recovery',
    name: 'PasswordRecovery',
    component: () => import("views/authentication/PasswordRecovery.vue"),
    meta: { requireUnauth: true }
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  // component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  store.dispatch('tryLogin');

  if (to.meta.requireAuth && !store.getters.isAuth) {
    next('/login');
  } else if (to.meta.requireUnauth && store.getters.isAuth) {
    next('/');
  } else {
    next();
  }
});

export default router;
