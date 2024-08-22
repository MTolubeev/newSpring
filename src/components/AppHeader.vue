<template>
  <header>
    <div class="info">
      <div class="profile">
        <img src="@/assets/Union.svg" alt="profile" />
        <router-link v-if="!user" to="/signin">
          <n-button style="--n-border-hover: 1px solid #fff;">Войти</n-button>
        </router-link>
        <div v-else>
          <span>{{ user.sub }}</span>
          <n-button @click="logout" style="--n-border-hover: 1px solid #fff;">Выйти</n-button>
        </div>
      </div>
      <div class="cart">
        <img src="@/assets/cart.svg" alt="cart" />
        <router-link to="/cart">
          <n-button style="--n-border-hover: 1px solid #fff;">Корзина</n-button>
        </router-link>
        <span>{{ cartItemCount }}</span>
      </div>
      <div class="main">
      <router-link to="/">
        <n-button style="--n-border-hover: 1px solid #fff; --n-text-color-focus: #f0f0f0; --n-border-focus: 1px solid #f0f0f0">Главная</n-button>
      </router-link>
    </div>
    </div>
    <div class="menu">
      <n-button style="--n-border-hover: 1px solid #fff;" @click="$emit('toggle-drawer')">Каталог</n-button>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useCartStore } from "@/store/cartStore";
import { NButton } from "naive-ui";
import { useRouter } from "vue-router";


const router = useRouter();

const cartStore = useCartStore();
const cartItemCount = ref(cartStore.cartItems.length);
const user = ref(null);

const decodeToken = (token) => {
  const base64Url = token.split(".")[1];
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
      .join("")
  );
  return JSON.parse(jsonPayload);
};
const fetchUser = () => {
  const token = localStorage.getItem("token");
  if (token) {
    user.value = decodeToken(token);
    console.log("Декодированный пользователь:", user.value);
  }else{
    console.log("Токен не найден. Пользователь не авторизован.");
  }
};
const logout = () => {
  localStorage.removeItem("token");
  user.value = null;
  router.push("/signin");
};

const updateCartItemCount = () => {
  cartItemCount.value = cartStore.cartItems.length;
};
watch(() => cartStore.cartItems.length, updateCartItemCount);

onMounted(() => {
  fetchUser();
  updateCartItemCount();
});
</script>

<style scoped>
header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 40px;
  background-color: #465a86;
}
.info {
  display: flex;
  gap: 30px;
}
.profile,
.cart, .menu {
  display: flex;
  align-items: center;
  gap: 10px;
}
.cart span{
  width: 40px;
  color: #fff;
}
.n-button{
  color: #fff;
}
.n-button:hover{
  color: #f0f0f0;
}
</style>
