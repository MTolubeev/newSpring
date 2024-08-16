<template>
  <MyHeader @toggle-drawer="toggleDrawer" />
  <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
  <div v-if="product">
    <h2 class="breadcrumb">
      <router-link class="product__link" :to="{ path: `/category/${productCategory}` }">
        {{ productCategory }}
      </router-link>
      /
      <span v-if="productSubcategory !== 'None'">
        <router-link class="product__link"
          :to="{ path: `/category/${productCategory}/subcategory/${productSubcategory}`,}">
          {{ productSubcategory }}
        </router-link>
        /
      </span>
      <span v-if="productSubsubcategory !== 'None'">
        <router-link class="product__link"
          :to="{ path: `/category/${productCategory}/subcategory/${productSubcategory}/subsubcategory/${productSubsubcategory}`,}">
          {{ productSubsubcategory }}
        </router-link>
        /
      </span>
      {{ product.title }}
    </h2>
    <div class="card__item">
      <n-card class="product-card">
        <div class="img-wrapper">
          <img
            v-if="product.imageUrl"
            :src="product.imageUrl"
            alt="Product Image"
            class="product-img"
          />
        </div>
        <div class="card__info">
          <h1 class="product-title">{{ product.title }}</h1>
          <p class="product-description">{{ product.description }}</p>
          <span>Цена: <b>{{ product.price }} руб.</b></span>
          <span>Количество товаров осталось: <b>{{ product.count }}</b></span>

          <CartButton v-if="product" :productId="product.id" :product="product" />
        </div>
      </n-card>
    </div>
  </div>
  <div v-else>
    <p>Loading...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
import { NCard } from "naive-ui";
import { useCartStore } from "@/store/cartStore"; 
import MyHeader from "@/components/AppHeader.vue";
import MyDrawer from "@/components/AppDrawer.vue";
import { useDrawer } from '@/composables/useHeader.js';

import CartButton from "@/components/CartButton.vue";


const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();
const route = useRoute();
const product = ref(null);
const cartStore = useCartStore(); 
const inCart = ref(false);

const fetchProduct = async (id) => {
  try {
    const response = await axios.get(`http://localhost:8080/product/getAll/${id}`);
    console.log('Product data:', response.data);
    const productData = response.data;

    if (productData.base64Image) {
      productData.imageUrl = `data:image/png;base64,${productData.base64Image}`;
    }

    product.value = productData;
    inCart.value = cartStore.isInCart(productData.id); 
  } catch (error) {
    console.error("Ошибка при получении продукта:", error);
  }
};

watch(() => route.params.productId, (newId) => {
  if (newId) {
    fetchProduct(newId);
    closeDrawer();
  }
});
const productCategory = computed(() => {
  return product.value?.categories?.[0]?.name || "Unknown";
});

const productSubcategory = computed(() => {
  return product.value?.categories?.[0]?.subcategory || "None";
});

const productSubsubcategory = computed(() => {
  return product.value?.categories?.[0]?.subsubcategory || "None";
});

onMounted(() => {
  const productId = route.params.productId;
  console.log('Loading product with ID:', productId);
  fetchProduct(productId);
});

</script>

<style scoped>
.breadcrumb {
  margin-bottom: 20px;
}

.card__item {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-card {
  width: 100%;
  display: flex;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.img-wrapper {
  flex: 1;
  margin-right: 20px;
}

.product-img {
  width: 300px;
  border-radius: 10px;
  object-fit: cover;
}

.card__info {
  flex: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.product-title {
  font-size: 28px;
  margin-bottom: 15px;
  color: #333;
}

.product-description {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.n-button {
  align-self: flex-start;
}
.n-card {
  width: 800px;
}
.product__link{
  text-decoration: none;
  color: inherit;
}
.product__link:hover{
  color: gray;
}

h2{
  margin: 10px 20px;
}
</style>
