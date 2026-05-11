import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Layout from '../views/Layout.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import BookList from '../views/book/BookList.vue'
import CategoryList from '../views/category/CategoryList.vue'
import BorrowList from '../views/borrow/BorrowList.vue'
import MyBorrow from '../views/borrow/MyBorrow.vue'
import UserList from '../views/user/UserList.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    {
      path: '/',
      component: Layout,
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', component: Dashboard, meta: { admin: true } },
        { path: 'books', component: BookList },
        { path: 'categories', component: CategoryList, meta: { admin: true } },
        { path: 'borrows', component: BorrowList, meta: { admin: true } },
        { path: 'my-borrows', component: MyBorrow },
        { path: 'users', component: UserList, meta: { admin: true } }
      ]
    }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.path !== '/login' && !auth.token) {
    return '/login'
  }
  if (to.meta.admin && !auth.isAdmin) {
    return '/books'
  }
  return true
})

export default router
