<template>
  <h2>Все товары</h2>
  <div class="cards">
    <MyCard
      v-for="item in items"
      :key="item.id"
      :item="item"
      @delete="handleDelete"
    ></MyCard>
  </div>
</template>

<script setup>
import MyCard from './CardItem.vue';
import { ref, onMounted, defineEmits } from 'vue';
import axios from 'axios';

const items = ref([]);
const emit = defineEmits(['products-loaded']);

const fetchItems = async () => {
  try {
    const response = await axios.get("http://localhost:8080/product/getAll");

    items.value = response.data.map((product) => {
      return {
        ...product,
        imageUrl: `data:image/png;base64,${product.base64Image}`,
      };
});
    emit('products-loaded');
  } catch (err) {
    console.log(err);
  }
};
const handleDelete = (itemId) => {
  items.value = items.value.filter((item) => item.id !== itemId);
};
onMounted(fetchItems);
</script>

<style scoped>
h2 {
  text-align: center;
  margin-top: 20px;
}
.cards {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-top: 20px;
  gap: 20px;
}
</style>
