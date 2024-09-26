<template>
  <n-button style="--n-border-hover: 1px solid #3B5998;" :style="buttonStyle" @click="toggleCart">
    {{ buttonText }}
  </n-button>
</template>

<script setup>
import { defineProps, ref, computed, onMounted } from "vue";
import { useCartStore } from "@/store/cartStore";
import { useUserStore } from "@/store/userStore";
import { NButton } from "naive-ui";
import { useRouter } from "vue-router";

const router = useRouter();
const cartStore = useCartStore();
const userStore = useUserStore();
const inCart = ref(false);

const props = defineProps({
  productId: {
    type: Number,
    required: true,
  },
  product: {
    type: Object,
    required: true,
  },
});

const user = computed(() => userStore.user.value);

const buttonText = computed(() =>
  inCart.value ? "Перейти в корзину" : "Добавить в корзину"
);

const buttonStyle = computed(() => ({
  backgroundColor: inCart.value ? "#f0f0f0" : "#3B5998",
  color: inCart.value ? "#000" : "#fff",
}));

const updateInCartStatus = async () => {
  try {
    const token = localStorage.getItem("token");
    await userStore.fetchUser();
    const userId = user.value?.id;
    if (userId) {
      const cart = await cartStore.fetchCart(userId, token);
      inCart.value = cart.some((item) => item.id === props.productId);
    } else {
      console.error("User ID is missing.");
    }
  } catch (error) {
    console.error("Error fetching user or cart:", error);
  }
};

const toggleCart = async () => {
  try {
    const token = localStorage.getItem("token");

    if (!token || !user.value) {
      return router.push("/signin");
    }
    if (!inCart.value) {
      await cartStore.addToCart(props.productId, token);
      inCart.value = true;
    } else {
      router.push("/cart");
    }
    await updateInCartStatus();
  } catch (error) {
    console.log(error);
  }
};

onMounted(async () => {
  await updateInCartStatus();
});
</script>
