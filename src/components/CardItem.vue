<template>
  <n-card size="huge" class="card">
    <div class="card-content">
      <router-link :to="{ path: `/product/${item.id}` }" class="card-link">
        <img :src="item.imageUrl" alt="png" />
        <h3>{{ item.title }}</h3>
        <p>{{ item.description }}</p>
        <div class="info_card">
          <span>
            Цена: <b>{{ item.price }} руб.</b>
          </span>
          <span>
            Количество товаров осталось: <b>{{ item.count }}</b>
          </span>
        </div>
      </router-link>
      <CartButton :productId="item.id" :product="item" />
    </div>
  </n-card>
</template>

<script setup>
import { defineProps } from 'vue';
import { NCard } from 'naive-ui';
import CartButton from './CartButton.vue';

defineProps({
  item: {
    type: Object,
    required: true,
  },
});
console.log()
</script>

<style scoped>
.card {
  width: 800px; 
  padding: 50px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}

.card-content {
  display: flex;
  flex-direction: column;
}

.card-link {
  text-decoration: none;
  color: inherit;
}

.card img {
  width: 150px; 
  height: auto; 
  max-height: 150px; 
  object-fit: contain; 
  margin-bottom: 15px; 
}

.card h3 {
  font-size: 24px;
  margin: 0 0 10px 0; 
}

.card p {
  font-size: 16px;
  margin: 0 0 20px 0; 
}

.info_card {
  display: flex;
  justify-content: space-between; 
  align-items: center;
  margin-top: auto;
}

.info_card span {
  font-size: 16px;
}

.info_card b {
  font-weight: bold;
}

.add-to-cart-button {
  position: absolute;
  bottom: 20px; 
  left: 20px; 
  right: 20px; 
  margin-top: 0; 
}

</style>


<style scoped>
.card {
  width: 800px; 
  padding: 50px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}

.card-content {
  display: flex;
  flex-direction: column;

}

.card-link {
  text-decoration: none;
  color: inherit;
}

.card img {
  width: 150px; 
  height: auto; 
  max-height: 150px; 
  object-fit: contain; 
  margin-bottom: 15px; 
}

.card h3 {
  font-size: 24px;
  margin: 0 0 10px 0; 
}

.card p {
  font-size: 16px;
  margin: 0 0 20px 0; 
}

.info_card {
  display: flex;
  justify-content: space-between; 
  align-items: center;
  margin-top: auto;
}

.info_card span {
  font-size: 16px;
}

.info_card b {
  font-weight: bold;
}

.add-to-cart-button {
  position: absolute;
  bottom: 20px; 
  left: 20px; 
  right: 20px; 
  margin-top: 0; 
}

</style>
