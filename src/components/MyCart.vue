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
import MyCard from "@/components/MyCard.vue";
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
  min-height: 90vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.cart-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.cart-item {
  width: 80%;

}
</style>
