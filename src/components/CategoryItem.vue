<template>
  <li>
    <router-link
      :to="{ name: 'Category', params: { categoryName: category.name } }">
      <strong>{{ category.name }}</strong>
    </router-link>

    <Draggable
      v-model="localSubcategories"
      :group="{ name: 'subcategories' }"
      item-key="name"
      :disabled="!editMode">
      <template #item="{ element: subcategory }">
        <ul>
          <li>
            <router-link
              :to="{
                name: 'Category',
                params: { categoryName: subcategory.name },
              }">
              {{ subcategory.name }}
            </router-link>

            <Draggable
              v-model="subcategory.products"
              :group="{ name: 'products' }"
              item-key="id"
              :disabled="!editMode">
              <template #item="{ element: product }">
                <ul>
                  <li>
                    <router-link
                      :to="{
                        name: 'ProductView',
                        params: { productId: product.id },}">
                      {{ product.title }}
                    </router-link>
                  </li>
                </ul>
              </template>
            </Draggable>
          </li>
        </ul>
      </template>
    </Draggable>

    <Draggable
      v-model="localProductsWithoutSubcategory"
      :group="{ name: 'products' }"
      item-key="id"
      :disabled="!editMode">
      <template #item="{ element: product }">
        <ul>
          <li>
            <router-link
              :to="{ name: 'ProductView', params: { productId: product.id } }">
              {{ product.title }}
            </router-link>
          </li>
        </ul>
      </template>
    </Draggable>
  </li>
</template>

<script setup>
import { defineProps, ref, watch } from "vue";
import Draggable from "vuedraggable";

const props = defineProps({
  category: Object,
  editMode: Boolean,
});


const localSubcategories = ref([...props.category.subcategories]);
const localProductsWithoutSubcategory = ref([
  ...props.category.productsWithoutSubcategory,
]);


watch(
  () => props.category.subcategories,
  (newVal) => {
    localSubcategories.value = [...newVal];
  }
);

watch(
  () => props.category.productsWithoutSubcategory,
  (newVal) => {
    localProductsWithoutSubcategory.value = [...newVal];
  }
);
</script>

<style scoped>
li {
  margin-bottom: 10px; 
  padding: 8px;
  border-radius: 4px;
}

ul {
  list-style: none;
  padding-left: 20px; 
}

a {
  text-decoration: none;
  color: #fff;
}

a:focus {
  outline: none;
}

a:hover {
  color: #2fd40e;
}
</style>
