<template>
  <div v-if="cartItems.length === 0" class="cart">
    <img src="@/assets/corob.svg" alt="corob" />
    <h3>Корзина пуста</h3>
    <p>Добавьте хотя бы один товар, чтобы сделать заказ.</p>
    <router-link to="/"><n-button type="success">Вернуться на главную</n-button></router-link>
  </div>
  <div v-else class="cart-items">
    <MyCard
      v-for="item in cartItems"
      :key="item.id"
      :item="item"
      @removeFromCart="removeFromCart"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useCartStore } from "@/store/cartStore";
import MyCard from "@/components/CardItem.vue";
import { NButton } from "naive-ui";

const cartStore = useCartStore();
const cartItems = ref(cartStore.cartItems);

const updateCartItems = () => {
  cartItems.value = cartStore.cartItems;
};

const removeFromCart = (itemId) => {
  cartStore.removeFromCart(itemId);
};

onMounted(updateCartItems);

watch(() => cartStore.cartItems, updateCartItems, { deep: true });
</script>

<style scoped>
.cart {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 15px;
}
.cart h3 {
  font-size: 24px;
  margin-bottom: 10px;
}

.cart p {
  font-size: 16px;
}

.cart-items {
  margin-top: 150px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}
</style>