<template>
  <div>
    <h2>Your Cart</h2>
    <ul v-if="cartItems.length">
      <li v-for="item in cartItems" :key="item.id">
        {{ item.title }} - {{ item.price }}
      </li>
    </ul>
    <p v-else>Your cart is empty.</p>
    <p v-if="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useCartStore } from "@/store/cartStore";
import { useUserStore } from "@/store/userStore";

const cartStore = useCartStore();
const { user, fetchUser } = useUserStore();

const cartItems = ref([]);
const error = ref("");

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
