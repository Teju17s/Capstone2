// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import BookFD from '@/components/BookFD.vue'
import FDList from '@/components/FDList.vue'

const routes = [
  { path: '/', redirect: '/book-fd' },
  { path: '/book-fd', name: 'BookFD', component: BookFD },
  { 
    path: '/fd-list',
    name: 'FDList',
    component: FDList,
    props: route => ({ newFdId: route.query.newFdId })
  },
  { path: '/:pathMatch(.*)*', redirect: '/book-fd' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
