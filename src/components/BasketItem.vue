<template>
    <n-card>
      <h4>{{ item.title }}</h4>
      <img width="200px" :src="item.imageUrl" alt="Product Image" v-if="item.imageUrl" class="product-image" />
      <span><strong>{{ item.price }} руб.</strong></span>
      <div class="controls">
        <button @click="decrementCount">-</button>
        <span>{{ localCount }}</span>
        <button @click="incrementCount">+</button>
      </div>
    </n-card>
  </template>
  
  <script setup>
  import { defineProps, ref, defineEmits, watch } from 'vue';
  import { useCartStore } from '@/store/cartStore';
  import { NCard } from 'naive-ui';
  
  const props = defineProps({
    item: {
      type: Object,
      required: true
    }
  });
  
  const emit = defineEmits(['updateItem']);

  const cartStore = useCartStore();
  const token = localStorage.getItem('token');
  
  const localCount = ref(props.item.count);
  
  const incrementCount = async () => {
    if (localCount.value >= 0) {
      localCount.value++;
      await cartStore.addToCart(props.item.id, token);
      emit('updateItem', { ...props.item, count: localCount.value });
    }
  };
  
const decrementCount = async () => {
if (localCount.value > 1) {
    localCount.value--;
    await cartStore.removeFromcart(props.item.id, token);
    emit('updateItem', { ...props.item, count: localCount.value });
}
};
  
watch(() => props.item.count, (newCount) => {
    localCount.value = newCount;
});
</script>

<style scoped>
.controls {
  display: flex;
  align-items: center;
}

.controls button {
  width: 30px;
  height: 30px;
  margin: 0 5px;
  text-align: center;
  line-height: 30px;
  font-size: 16px;
  cursor: pointer;
}

.controls span {
  font-size: 16px;
}
</style>
