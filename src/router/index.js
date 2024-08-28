import { createRouter, createWebHashHistory } from "vue-router";
import MyHome from "@/pages/MainPage.vue";
import CartPage from "@/pages/CartPage.vue";
import RegistrPage from "@/pages/RegistrPage.vue";
import CategoriesView from "@/pages/CategoriesView.vue";
import ProductItem from "@/pages/ProductItem.vue";
import SignPage from "@/pages/SignPage.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: MyHome,
  },
  {
    path: "/cart",
    name: "Cart",
    component: CartPage,
  },
  {
    path: "/registration",
    name: "Register",
    component: RegistrPage,
  },
  {
    path: "/signin",
    name: "SignIn",
    component: SignPage,
  },
  {
    path: "/category/:categoryName/:subcategoryName?/:subsubcategoryName?",
    name: "Category",
    component: CategoriesView,
    props: true,
  },
  {
    path: "/product-view/:productId",
    name: "ProductView",
    component: ProductItem,
    props: true,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
