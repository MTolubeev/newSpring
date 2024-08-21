<template>
  <n-card size="huge" class="card" @click="navigateToproduct" hoverable>
      <div class="card-content">
        <div class="image-container">
          <img :src="item.imageUrl" alt="png" />
        </div>
        <h3>{{ item.title }}</h3>
        <div class="info_card">
          <span>
            Цена: <b>{{ item.price }} руб.</b>
          </span>
          <span>
            Количество товаров осталось: <b>{{ item.count }}</b>
          </span>
        </div>
      </div>
    <div class="card__button">
    <CartButton :productId="item.id" :product="item" @click.stop />
    </div>
  </n-card>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { defineProps } from 'vue';
import { NCard } from 'naive-ui';
import CartButton from './CartButton.vue';

const router = useRouter();

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const navigateToproduct = () =>{
  router.push({ path: `/product-view/${props.item.id}` })

}
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
  cursor: pointer;
}

.card-content {
  display: flex;
  flex-direction: column;
}
.image-container {
  width: 300px;
  height: 300px;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: contain; 
}
.n-button{
  width: 100%;
}
.card__button{
  margin-top: 20px;
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
</style>

