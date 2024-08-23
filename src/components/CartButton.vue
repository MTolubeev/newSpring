<template>
  <n-button :style="buttonStyle" @click="toggleCart">
    {{ buttonText }}
  </n-button>
</template>

<script setup>
import { defineProps, ref, computed, onMounted } from "vue";
import { useCartStore } from '@/store/cartStore';
import { useUserStore } from '@/store/userStore'
const { user, fetchUser } = useUserStore(); 
const cartStore = useCartStore();
const inCart = ref(false)
import { NButton } from "naive-ui";


 const props  = defineProps({
  productId: {
    type: Number,
    required: true,
  },
  product: {
    type: Object,
    required: true
  }
});



const toggleCart = async() =>{
  try{
    const token = localStorage.getItem("token");
    if(!inCart.value){
      await cartStore.addToCart(props.productId, token)
      inCart.value = true;
    }else{
      await cartStore.removeFromcart(props.productId, token);
      inCart.value = false;
    }
    updateInCartStatus();
  }catch(error){
    console.log(error)
  }
}

const buttonText = computed(() =>
     inCart.value ? "Удалить из корзины" : "Добавить в корзину"
 );

 const buttonStyle = computed(() =>({
   backgroundColor: inCart.value ? "#2d4373" : "#3B5998",
   color: "#fff"
 }))

 const updateInCartStatus = async () => {
  try {
    const token = localStorage.getItem("token");
    await fetchUser();
    const userId = user.value?.id;
    if (userId) {
      const cart = await cartStore.fetchCart(userId, token);
      inCart.value = cart.some(item => item.id === props.productId);
    }
  } catch (error) {
    console.error("Error fetching user or cart:", error);
  }
};
onMounted(() => {
  updateInCartStatus();
});
</script>