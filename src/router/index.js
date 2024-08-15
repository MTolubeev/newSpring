import { createRouter, createWebHashHistory } from 'vue-router'
import MyHome from '@/pages/MainPage.vue'
import CartPage from '@/pages/CartPage.vue'
import registrPage from '@/pages/RegistrPage.vue'
import CategoriesView from '@/pages/CategoriesView.vue'
import ProductItem from '@/pages/ProductItem.vue'
import SignPage from '@/pages/SignPage.vue'

const routes = [
  {
    path: '/',
    component: MyHome
  },
  {
    path: '/cart',
    component: CartPage
  },
  {
    path: '/registration',
    component: registrPage
  },
  {
    path: '/signin',
    component: SignPage
  },
  {
    path: "/category/:categoryName",
    component: CategoriesView,
    props: true
  },
  {
    path: "/category/:categoryName/subcategory/:subcategoryName",
    component: CategoriesView,
    props: true,
  },
  {
    path: "/category/:categoryName/subcategory/:subcategoryName/subsubcategory/:subsubcategoryName",
    component: CategoriesView,
    props: true,
  },
  {
    path: "/product/:productId",
    component: ProductItem,
    props: true
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
