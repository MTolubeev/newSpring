<template>
  <div>
    <h2>Ваша коризна</h2>
    <ul v-if="cartItems.length">
     <BasketItem v-for="item in cartItems" :key="item.id" :item="item"
     @updateItem="updateCartItem" /> 
    </ul>
    <p v-else>Your cart is empty.</p>
    <p v-if="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useCartStore } from "@/store/cartStore";
import { useUserStore } from "@/store/userStore";
import BasketItem from "./BasketItem.vue";

const cartStore = useCartStore();
const { user, fetchUser } = useUserStore();

const cartItems = ref([]);
const error = ref("");

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
