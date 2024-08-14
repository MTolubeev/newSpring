<template>
    <div v-if="isVisible" class="catalog">
      <div class="cart">
        <h2>каталог товаров</h2>
        <svg
          @click="$emit('close-drawer')" 
          width="16" height="14" viewBox="0 0 16 14" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M1 7H14.7143" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
          <path d="M8.71436 1L14.7144 7L8.71436 13" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <ul>
      <li v-for="category in categories" :key="category.name">
        <router-link :to="{ path: `/category/${category.name}` }">
          <strong>{{ category.name }}</strong>
        </router-link>
        <ul>
          <li v-for="subcategory in category.subcategories" :key="subcategory.name">
            <router-link :to="{ path: `/category/${category.name}/subcategory/${subcategory.name}` }">
              <strong>{{ subcategory.name }}</strong>
            </router-link>
            <ul>
              <li v-for="subsubcategory in subcategory.subsubcategories" :key="subsubcategory.name">
                <router-link :to="{ path: `/category/${category.name}/subcategory/${subcategory.name}/subsubcategory/${subsubcategory.name}` }">
                  <strong>{{ subsubcategory.name }}</strong>
                </router-link>
                <ul>
                  <li v-for="product in subsubcategory.products" :key="product.id">
                    <router-link :to="{ path: `/product/${product.id}` }">
                      {{ product.title }}
                    </router-link>
                  </li>
                </ul>
              </li>
              <li v-for="product in subcategory.products" :key="product.id">
                <router-link :to="{ path: `/product/${product.id}` }">
                  {{ product.title }}
                </router-link>
              </li>
            </ul>
          </li>
          <li v-for="product in category.productsWithoutSubcategory" :key="product.id">
            <router-link :to="{ path: `/product/${product.id}` }">
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

const categories = ref([]);

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

const organizeProductsByCategories = (products) => {
  const categoryMap = new Map();

  products.forEach(product => {
    product.categories.forEach(cat => {
      let category = categoryMap.get(cat.name);
      if (!category) {
        category = { name: cat.name, subcategories: [], productsWithoutSubcategory: [] };
        categoryMap.set(cat.name, category);
      }

      if (!cat.subcategory || cat.subcategory === '') {
        category.productsWithoutSubcategory.push(product);
      } else {
        let subcategory = category.subcategories.find(sub => sub.name === cat.subcategory);
        if (!subcategory) {
          subcategory = { name: cat.subcategory, products: [], subsubcategories: [] };
          category.subcategories.push(subcategory);
        }

        if (cat.subsubcategory) {
          let subsubcategory = subcategory.subsubcategories.find(subsub => subsub.name === cat.subsubcategory);
          if (!subsubcategory) {
            subsubcategory = { name: cat.subsubcategory, products: [] };
            subcategory.subsubcategories.push(subsubcategory);
          }
          subsubcategory.products.push(product);
        } else {
          subcategory.products.push(product);
        }
      }
    });
  });

  return Array.from(categoryMap.values());
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
  background-color: #98fb98;
  padding: 1.75rem 2.5rem;
  top: 0;
  right: 0;
  z-index: 10;
  overflow-y: auto; /* Добавлено, чтобы список не вылезал за пределы экрана */
}

h2,
span {
  font-family: Arial, Helvetica, sans-serif;
}

.cart {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

svg {
  cursor: pointer;
}

/* Удаление точек списка */
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

/* Эффект изменения цвета при наведении на ссылку */
a:hover {
  color: #007bff; /* Измените на желаемый цвет */
}

li {
  margin-bottom: 0.5rem;
  padding: 0.5rem; /* Добавлено для улучшения взаимодействия */
  border-radius: 4px; /* Скругленные углы для лучшего визуального восприятия */
}

ul ul {
  padding-left: 1rem; /* Отступ для вложенных списков */
}

/* Удаление фона для списка и ссылок при наведении на родительский блок */
.catalog:hover ul,
.catalog:hover a {
  background-color: transparent; /* Чтобы не было нежелательных эффектов при наведении на родительский блок */
}
  </style>
  