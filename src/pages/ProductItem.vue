<template>
  <n-spin content-style="--n-opacity-spinning:0; height: 100vh;" stroke="blue" :show="loader">
    <div v-if="product">
      <MyHeader @toggle-drawer="toggleDrawer" />
      <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
      <h2 class="breadcrumb">
        <router-link
            class="product__link"
            :to="{ name: 'Category', params: { categoryName: productCategory } }">
          {{ productCategory }}
        </router-link>
        /
        <span v-if="productSubcategory !== 'None'">
          <router-link
              class="product__link"
              :to="{ name: 'Category', params: { categoryName: productCategory, subcategoryName: productSubcategory } }">
            {{ productSubcategory }}
          </router-link>
          /
        </span>
        <span v-if="productSubsubcategory !== 'None'">
          <router-link
              class="product__link"
              :to="{ name: 'Category', params: { categoryName: productCategory, subcategoryName: productSubcategory, subsubcategoryName: productSubsubcategory } }">
            {{ productSubsubcategory }}
          </router-link>
          /
        </span>
        {{ product.title }}
      </h2>
      <div class="card__item">
        <n-card class="product-card" content-style="display: flex; flex-direction: row !important;">
          <div class="img-wrapper">
            <img
                v-if="product.imageUrl"
                :src="product.imageUrl"
                alt="Product Image"
                class="product-img"/>
          </div>
          <div class="card__info">
            <h1 class="product-title">{{ product.title }}</h1>
            <p class="product-description">{{ product.description }}</p>
            <div class="card__pay">
              <span v-if="isAuthenicated">Цена: <b>{{ product.price }} руб.</b>
                  <del style="margin-left: 10px">{{ product.discountPrice }} руб.</del>
              </span>
              <span v-else>Цена: {{ product.price }}</span>
              <span>Количество товаров осталось: <b>{{ product.count }}</b></span>
              <CartButton 
              v-if="product" 
              :productId="product.id" 
              :product="product" 
              />
            </div>
          </div>
        </n-card>
      </div>
      <ProductsComment 
      :comments="product.comments" 
      :productId="product.id" 
      />
    </div>
  </n-spin>
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
import { NCard, NSpin } from "naive-ui";
import MyHeader from "@/components/AppHeader.vue";
import MyDrawer from "@/components/AppDrawer.vue";
import { useDrawer } from '@/composables/useHeader.js';
import ProductsComment from "@/components/ProductsComment.vue";
import CartButton from "@/components/BasketButton.vue";


const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();
const route = useRoute();
const product = ref(null);

const isAuthenicated = ref(false);

const loader = computed(() => !product.value);
const productCategory = computed(() => {
  return product.value?.categories?.[0]?.name || "Unknown";
});

const productSubcategory = computed(() => {
  return product.value?.categories?.[0]?.subcategory || "None";
});

const productSubsubcategory = computed(() => {
  return product.value?.categories?.[0]?.subsubcategory || "None";
});

const fetchProduct = async (id) => {
  try {
    const response = await axios.get(`http://localhost:8080/product/getAll/${id}`);
    const productData = response.data;

    if (productData.base64Image) {
      productData.imageUrl = `data:image/png;base64,${productData.base64Image}`;
    }

    product.value = productData;
  } catch (error) {
    console.error("продукт не получили:", error);
  }
};

const checkAuth = () =>{
  const token = localStorage.getItem("token");
  if(token){
    isAuthenicated.value = true;
  }
}

watch(() => route.params.productId, (newId) => {
  if (newId) {
    fetchProduct(newId);
    closeDrawer();
  }
});

onMounted(() => {
  checkAuth();
  const productId = route.params.productId;
  fetchProduct(productId);
});
</script>

<style scoped>
.breadcrumb {
  margin-bottom: 20px;
}
.card__item {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.product-card {
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.img-wrapper {
  flex: 1;
}

.product-img {
  width: 350px;
  object-fit: cover;
}

.card__info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.card__pay{
  display: flex;
  flex-direction: column;
  gap: 15px;
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
  width: 100%;
}
.n-card {
  width: 1000px;
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
