<template>
  <div class="cards">
    <h2>Все товары</h2>
    <MyCard v-for="item in items" :key="item.id" :item="item"></MyCard>
  </div>
</template>

<script setup>
import MyCard from "./CardItem.vue";
import { ref, onMounted } from "vue";
import axios from "axios";

const items = ref([]);

const fetchItems = async () => {
  try {
    const response = await axios.get("http://localhost:8080/product/getAll");

    items.value = response.data.map((product) => {
      return {
        ...product,
        imageUrl: `data:image/png;base64,${product.base64Image}`, 
      };
    });
    console.log(items)

  } catch (err) {
    console.log(err);
  }
};

onMounted(fetchItems);
</script>

<style scoped>
.cards {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
  gap: 20px;
}
</style>
