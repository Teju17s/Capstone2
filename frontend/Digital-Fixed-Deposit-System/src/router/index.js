// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import BookFD from '@/components/BookFD.vue';
import FDList from '@/components/FDList.vue';

const routes = [
  { path: '/', redirect: '/book-fd' },
  { path: '/book-fd', name: 'BookFD', component: BookFD },
  { 
    path: '/fd-list', 
    name: 'FDList', 
    component: FDList,
    // Ensure that query params trigger component reload if needed
    props: route => ({ newFdId: route.query.newFdId })
  },
  { path: '/:pathMatch(.*)*', redirect: '/book-fd' } // catch-all redirect
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // Maintain scroll for navigation or scroll to top
    if (savedPosition) return savedPosition;
    return { top: 0 };
  }
});

export default router;

