<template>
  <Draggable
    v-model="internalCategories"
    :group="{ name: 'categories' }"
    @end="handleDragEnd"
    item-key="name"
    :disabled="!editMode">
    <template #item="{ element }">
      <li>
        <router-link :to="{ name: 'Category', params: { categoryName: element.name } }">
          <strong>{{ element.name }}</strong>
        </router-link>

        <Draggable
          v-model="element.subcategories"
          :group="{ name: 'subcategories' }"
          item-key="name"
          :disabled="!editMode">
          <template #item="{ element: subcategory }">
            <ul>
              <li>
                <router-link :to="{ name: 'Category', params: { categoryName: element.name, subcategoryName: subcategory.name } }">
                  <strong>{{ subcategory.name }}</strong>
                </router-link>

                <Draggable
                  v-model="subcategory.subsubcategories"
                  :group="{ name: 'subsubcategories' }"
                  item-key="name"
                  :disabled="!editMode">
                  <template #item="{ element: subsubcategory }">
                    <ul>
                      <li>
                        <router-link :to="{ name: 'Category', params: { categoryName: element.name, subcategoryName: subcategory.name, subsubcategoryName: subsubcategory.name } }">
                          <strong>{{ subsubcategory.name }}</strong>
                        </router-link>

                        <Draggable
                          v-model="subsubcategory.products"
                          :group="{ name: 'products' }"
                          item-key="id"
                          :disabled="!editMode">
                          <template #item="{ element: product }">
                            <ul>
                              <li>
                                <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">
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
                  v-model="subcategory.products"
                  :group="{ name: 'products' }"
                  item-key="id"
                  :disabled="!editMode">
                  <template #item="{ element: product }">
                    <ul>
                      <li>
                        <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">
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
          v-model="element.productsWithoutSubcategory"
          :group="{ name: 'products' }"
          item-key="id"
          :disabled="!editMode">
          <template #item="{ element: product }">
            <ul>
              <li>
                <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">
                  {{ product.title }}
                </router-link>
              </li>
            </ul>
          </template>
        </Draggable>
      </li>
    </template>
  </Draggable>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch } from 'vue';
import Draggable from 'vuedraggable';

const emit = defineEmits(['drag-end']);

const props = defineProps({
  categories: Array,
  editMode: Boolean
});

const internalCategories = ref([...props.categories]);

watch(() => props.categories, (newCategories) => {
  internalCategories.value = [...newCategories];
});

const handleDragEnd = () => {
  emit('drag-end', internalCategories.value);
};
</script>

<style scoped>
a:focus {
  outline: none;
}
a:hover {
  color: #2fd40e;
}

li {
  margin-bottom: 8px;
  padding: 8px;
  border-radius: 4px;
  list-style-type: none;
}

ul {
  padding-left: 16px;
}

a {
  text-decoration: none;
  color: white;
}

</style>
