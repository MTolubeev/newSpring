<template>
  <div class="cart" v-if="cartItems.length">
    <h2>Ваша корзина</h2>
    <div class="cart__info">
      <div class="products__list">
        <BasketItem
          v-for="item in cartItems"
          :key="item.id"
          :item="item"
          @updateItem="updateCartItem"
        />
      </div>
      <div class="info__controll">
        <span>Всего товаров:{{ totalItems }}</span>
        <span>Общая сумма: {{ totalPrice }} ₽</span>
        <n-button class="button" type="success">Оформить заказ</n-button>
      </div>
    </div>
  </div>
  <div v-else class="cart__empty">
    <h2>Корзина пустая</h2>
    <img src="@/assets/corob.svg" alt="Пустая корзина" />
    <p>
      Ввойдите или зарегестрируйтесь, чтобы вы смогли добалять товары в корзину
    </p>
  </div>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { useCartStore } from "@/store/cartStore";
import { useUserStore } from "@/store/userStore";
import BasketItem from "./BasketItem.vue";
import { NButton } from "naive-ui";

const cartStore = useCartStore();
const userStore = useUserStore();
const user = computed(() => userStore.user.value);

const totalItems = computed(() => {
  return cartStore.cartItems.reduce((total, item) => total + item.count, 0);
});

const totalPrice = computed(() => {
  return cartStore.cartItems.reduce(
    (total, item) => total + item.price * item.count,
    0
  );
});

const updateCartItem = (updatedItem) => {
  if (updatedItem === null) {
    cartStore.cartItems = cartStore.cartItems.filter(
      (item) => item.id !== undefined
    );
  } else {
    const index = cartStore.cartItems.findIndex(
      (item) => item.id === updatedItem.id
    );
    if (index !== -1) {
      cartStore.cartItems[index] = updatedItem;
    }
  }
};

onMounted(async () => {
  try {
    await userStore.fetchUser();
    if (user.value && user.value.id) {
      const token = localStorage.getItem("token");
      await cartStore.fetchCart(user.value.id, token);
    } else {
      console.error("User not authenticated or user ID is missing.");
    }
  } catch (err) {
    console.error("Failed to load user or cart:", err.message);
  }
});

const cartItems = computed(() => cartStore.cartItems);
</script>

<style scoped>
.button {
  margin-top: auto;
}
.cart__info {
  display: flex;
  justify-content: space-around;
}
.info__controll {
  background-color: #fff;
  width: 600px;
  height: 400px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}
.info__controll span {
  font-size: 30px;
}
.cart__empty {
  display: flex;
  height: 100vh;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  gap: 20px;
}
p {
  text-align: center;
  width: 350px;
  font-size: 16px;
}
.cart {
  margin-top: 100px;
}
h2 {
  text-align: center;
}
.n-card {
  width: 600px;
}
</style>
