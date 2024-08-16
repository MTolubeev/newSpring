<template>
  <n-card size="huge" class="card" @click="navigateToproduct">
      <div class="card-content">
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
      </div>
    <CartButton :productId="item.id" :product="item" @click.stop />
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

