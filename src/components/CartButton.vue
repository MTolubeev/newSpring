<template>
  <n-button :style="buttonStyle" @click="toggleCart">
    {{ buttonText }}
  </n-button>
</template>

<script setup>
import { computed, ref, defineProps, watch, onMounted } from "vue";
import { useCartStore } from "@/store/cartStore";
import { NButton } from "naive-ui";

const props = defineProps({
  productId: {
    type: Number,
    required: true,
  },
  product: {
    type: Object,
    required: true
  }
});

const cartStore = useCartStore();
const inCart = ref(false);


onMounted(() => {
  inCart.value = cartStore.isInCart(props.productId);
});

// const buttonType = computed(() => (inCart.value ? "default" : "success"));

const buttonText = computed(() =>
  inCart.value ? "Удалить из корзины" : "Добавить в корзину"
);

const buttonStyle = computed(() =>({
  backgroundColor: inCart.value ? "#2d4373" : "#3B5998",
  color: "#fff"
}))


const toggleCart = () => {
  if (props.product && props.product.id) {
    if (inCart.value) {
      cartStore.removeFromCart(props.productId);
    } else {
      cartStore.addToCart(props.product);
    }
    inCart.value = !inCart.value;
  } else {
    console.error('Ошибка: некорректный товар в CartButton', props.product);
  }
};

watch(() => cartStore.cartItems,
  () => {
    inCart.value = cartStore.isInCart(props.productId);
  },
  { immediate: true }
);

</script>
