<template>
  <div v-if="isVisible" class="catalog">
    <div class="cart">
      <h2>Каталог товаров</h2>
      <svg
          @click="$emit('close-drawer')"
          width="16" height="14" viewBox="0 0 16 14" fill="#fff" xmlns="http://www.w3.org/2000/svg">
        <path d="M1 7H14.7143" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
        <path d="M8.71436 1L14.7144 7L8.71436 13" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    </div>
    <ul>
      <li v-for="category in categories" :key="category.name">
        <router-link :to="{ name: 'Category', params: { categoryName: category.name } }">
          <strong>{{ category.name }}</strong>
        </router-link>
        <ul>
          <li v-for="subcategory in category.subcategories" :key="subcategory.name">
            <router-link :to="{ name: 'Category', params: { categoryName: category.name, subcategoryName: subcategory.name } }">
              <strong>{{ subcategory.name }}</strong>
            </router-link>
            <ul>
              <li v-for="subsubcategory in subcategory.subsubcategories" :key="subsubcategory.name">
                <router-link :to="{ name: 'Category', params: { categoryName: category.name, subcategoryName: subcategory.name, subsubcategoryName: subsubcategory.name } }">
                  <strong>{{ subsubcategory.name }}</strong>
                </router-link>
                <ul>
                  <li v-for="product in subsubcategory.products" :key="product.id">
                    <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">
                      {{ product.title }}
                    </router-link>
                  </li>
                </ul>
              </li>
              <li v-for="product in subcategory.products" :key="product.id">
                <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">
                  {{ product.title }}
                </router-link>
              </li>
            </ul>
          </li>
          <li v-for="product in category.productsWithoutSubcategory" :key="product.id">
            <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">
              {{ product.title }}
            </router-link>
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>
  
  <script setup>
  import { defineProps, ref, onMounted } from 'vue';
  import axios from 'axios';
  
  import { useOrganizeProducts } from '@/composables/useOrganizeProducts';
  
  const categories = ref([]);
  const { organizeProductsByCategories } = useOrganizeProducts();
  
  defineProps({
    isVisible: Boolean
  });
  
  const fetchData = async () => {
    try {
      const response = await axios.get('http://localhost:8080/product/getAll');
      const products = response.data;
  
      if (Array.isArray(products)) {
        categories.value = organizeProductsByCategories(products);
      } else {
        console.error('Неправильный формат данных:', products);
      }
    } catch (error) {
      console.error('Ошибка при получении данных:', error);
    }
  };
  
  onMounted(() => {
    fetchData();
  });
  </script>
  
<style scoped>
.catalog {
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100%;
  width: 24rem;
  background-color: #465a86;
  padding: 1.75rem 2.5rem;
  top: 0;
  right: 0;
  z-index: 10;
  border-left: 3px solid #f0f0f0;

}
.catalog a{
  color: #fff;
}

h2,
span {
  font-family: Arial, Helvetica, sans-serif;
  color: #fff;
}

.cart {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

svg {
  cursor: pointer;
}


ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}


a {
  text-decoration: none;
  color: black; 
  display: block; 
}

a:focus {
  outline: none;
}
a:hover {
  color: #007bff; 
}

li {
  margin-bottom: 0.5rem;
  padding: 0.5rem; 
  border-radius: 4px; 
}

ul ul {
  padding-left: 1rem; 
}


.catalog:hover ul,
.catalog:hover a {
  background-color: transparent; 
}
  </style>
  