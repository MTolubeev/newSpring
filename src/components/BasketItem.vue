<template>
    <n-card content-style="display: flex; flex-direction:column; gap:20px">
      <h2>{{ item.title }}</h2>
      <img width="200px" :src="item.imageUrl" alt="Product Image" v-if="item.imageUrl" class="product-image" />
      <div class="controls">
        <n-button @click="decrementCount">-</n-button>
        <span>{{ localCount }}</span>
        <n-button @click="incrementCount">+</n-button>
      </div>
      <span class="item__price"><strong>{{ item.price }} руб.</strong></span>
    </n-card>
  </template>
  
  <script setup>
  import { defineProps, ref, defineEmits, watch } from 'vue';
  import { useCartStore } from '@/store/cartStore';
  import { NCard, NButton } from 'naive-ui';
  
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
    if (localCount.value > 0) {
      localCount.value--;
      await cartStore.removeFromCart(props.item.id, token);
      emit('updateItem', { ...props.item, count: localCount.value });
    }
  };
  // const removeItem = async () => {
  //   try {
  //     await cartStore.removeAllFromCart(props.item.id, token); // Удаляем товар полностью
  //     emit('updateItem', { ...props.item, count: 0 }); // Обновляем родительский компонент
  //   } catch (error) {
  //     console.error('Ошибка удаления товара:', error);
  //   }
  // };
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
  width: 40px;
  height: 40px; 
  margin: 0 5px;
  text-align: center;
  font-size: 18px; 
  cursor: pointer;
}

.controls span {
  font-size: 18px;
  width: 40px; 
  text-align: center; 
  font-family: 'Arial', sans-serif;
}
.item__price{
  font-size: 20px;
}
</style>
