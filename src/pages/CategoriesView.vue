<template>
  <MyHeader @toggle-drawer="toggleDrawer" />
  <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
  <div class="paths">
    <h1>
      <router-link
        class="path__link"
        :to="{ path: `/category/${categoryName}` }"
        >{{ categoryName }}</router-link
      >
      /
      <router-link
        class="path__link"
        v-if="subcategoryName"
        :to="{
          path: `/category/${categoryName}/subcategory/${subcategoryName}`,
        }"
      >
        {{ subcategoryName }}
      </router-link>
      <span v-if="subsubcategoryName">
        /
        <router-link
          class="path__link"
          :to="{
            path: `/category/${categoryName}/subcategory/${subcategoryName}/subsubcategory/${subsubcategoryName}`,
          }"
        >
          {{ subsubcategoryName }}
        </router-link>
      </span>
    </h1>
  </div>
  <div class="cards">
    <MyCard
      v-for="product in filteredProducts"
      :key="product.id"
      :item="product"
    ></MyCard>
  </div>
  <router-link :to="{ path: '/' }">Вернуться к списку категорий</router-link>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
import MyCard from "@/components/CardItem.vue";
import { useDrawer } from "@/composables/useHeader.js";
import MyHeader from "@/components/AppHeader.vue";
import MyDrawer from "@/components/AppDrawer.vue";

const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();
const route = useRoute();
const categories = ref([]);
const categoryName = ref(route.params.categoryName);
const subcategoryName = ref(route.params.subcategoryName || "");
const subsubcategoryName = ref(route.params.subsubcategoryName || "");

const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/product/getAll");
    const products = response.data;

    if (Array.isArray(products)) {
      products.forEach((product) => {
        product.imageUrl = `data:image/png;base64,${product.base64Image}`;
      });
      categories.value = organizeProductsByCategories(products);
    } else {
      console.error("Неправильный формат данных:", products);
    }
  } catch (error) {
    console.error("Ошибка при получении данных:", error);
  }
};

const organizeProductsByCategories = (products) => {
  const categoryMap = new Map();

  products.forEach((product) => {
    product.categories.forEach((cat) => {
      let category = categoryMap.get(cat.name);
      if (!category) {
        category = {
          name: cat.name,
          subcategories: [],
          productsWithoutSubcategory: [],
        };
        categoryMap.set(cat.name, category);
      }

      if (!cat.subcategory || cat.subcategory === "") {
        category.productsWithoutSubcategory.push(product);
      } else {
        let subcategory = category.subcategories.find(
          (sub) => sub.name === cat.subcategory
        );
        if (!subcategory) {
          subcategory = {
            name: cat.subcategory,
            products: [],
            subsubcategories: [],
          };
          category.subcategories.push(subcategory);
        }

        if (cat.subsubcategory) {
          let subsubcategory = subcategory.subsubcategories.find(
            (subsub) => subsub.name === cat.subsubcategory
          );
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

const filteredProducts = computed(() => {
  const category = categories.value.find(
    (cat) => cat.name === categoryName.value
  );

  if (!category) return [];

  if (!subcategoryName.value) {
    return [
      ...category.productsWithoutSubcategory,
      ...category.subcategories.flatMap((sub) => [
        ...sub.products,
        ...sub.subsubcategories.flatMap((subsub) => subsub.products),
      ]),
    ];
  }
  const subcategory = category.subcategories.find(
    (sub) => sub.name === subcategoryName.value
  );

  if (!subcategory) return [];
  if (!subsubcategoryName.value) {
    return [
      ...subcategory.products,
      ...subcategory.subsubcategories.flatMap((subsub) => subsub.products),
    ];
  }
  const subsubcategory = subcategory.subsubcategories.find(
    (subsub) => subsub.name === subsubcategoryName.value
  );
  return subsubcategory ? subsubcategory.products : [];
});

watch(
  () => route.params,
  (newParams) => {
    categoryName.value = newParams.categoryName;
    subcategoryName.value = newParams.subcategoryName || "";
    subsubcategoryName.value = newParams.subsubcategoryName || "";
    fetchData();
  },
  { immediate: true }
);

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.paths {
  margin: 10px 20px;
}
.paths .path__link {
  text-decoration: none;
  color: inherit;
}

.path__link:hover {
  color: gray;
}
.cards {
  display: flex;

  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
}
</style>
