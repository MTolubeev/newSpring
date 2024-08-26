<template>
    <div class="cart" v-if="cartItems.length">
      <h2>Ваша корзина</h2>
      <div class="products__list">
     <BasketItem v-for="item in cartItems" :key="item.id" :item="item"
     @updateItem="updateCartItem" />
      </div>
      <div class="info__controll">
        <span>Всего товаров:{{ totalItems }}</span>
        <span>Общая сумма: {{ totalPrice }} ₽</span>
      </div>
    </div>
    <div v-else class="cart__empty">
      <h2>Корзина пустая</h2>
    <img src="@/assets/corob.svg" alt="Пустая корзина">
      <p>Ввойдите или зарегестрируйтесь, чтобы вы смогли добалять товары в корзину</p>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { useCartStore } from "@/store/cartStore";
import { useUserStore } from "@/store/userStore";
import BasketItem from "./BasketItem.vue";

const cartStore = useCartStore();
const { user, fetchUser } = useUserStore();

const cartItems = ref([]);
const error = ref("");

const totalItems = computed(() => {
  return cartStore.cartItems.reduce((total, item) => total + item.count, 0);
});
const totalPrice = computed(() => {
  return cartStore.cartItems.reduce((total, item) => total + item.price * item.count, 0);
});
const updateCartItem = (updatedItem) => {
  const index = cartItems.value.findIndex(item => item.id === updatedItem.id);
  if (index !== -1) {
    cartItems.value[index] = updatedItem; 
  }
};

watch(() => cartStore.cartItems, (newItems) => {
  cartItems.value = [...newItems];
}, { deep: true });

onMounted(async () => {
  fetchUser();
  if (user.value) {
    const token = localStorage.getItem("token");
    try {
      const products = await cartStore.fetchCart(user.value.id, token);
      cartItems.value = products;
    } catch (err) {
      error.value = "Failed to load cart: " + err.message;
    }
  } else {
    error.value = "User not authenticated.";
  }
});
</script>

<style scoped>
.cart__empty{
  display: flex;
  height: 100vh;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  gap: 20px;
}
p{
  text-align: center;
  width: 350px;
  font-size: 16px;
}
.cart{
  margin-top: 100px;
}
h2{
  text-align: center;
}
</style>