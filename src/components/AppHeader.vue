<template>
  <header>
    <div class="info">
      <div class="profile">
        <img src="@/assets/Union.svg" alt="profile" />
        <router-link to="/signin">
          <n-button style="--n-border-hover: 1px solid #fff;">Войти</n-button>
        </router-link>
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

const cartStore = useCartStore();
const cartItemCount = ref(cartStore.cartItems.length);

const updateCartItemCount = () => {
  cartItemCount.value = cartStore.cartItems.length;
};
watch(() => cartStore.cartItems.length, updateCartItemCount);

onMounted(updateCartItemCount);
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
