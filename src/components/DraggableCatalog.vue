<template>
  <Draggable
    v-model="internalCategories"
    :group="{ name: 'categories' }"
    @end="handleDragEnd"
    item-key="name"
    :disabled="!editMode">
    <template #item="{ element }">
      <li class="category-item">
        <div class="category-content">
          <router-link :to="{ name: 'Category', params: { categoryName: element.name } }" class="category-link">
            <strong>{{ element.name }}</strong>
          </router-link>
         
          <span v-if="editMode" class="edit-icon" @click="handleEditCategory(element)">
            <img src="@/assets/pencil.svg" alt="pencil">
          </span>
        </div>

        <Draggable
          v-model="element.subcategories"
          :group="{ name: 'subcategories' }"
          item-key="name"
          :disabled="!editMode">
          <template #item="{ element: subcategory }">
            <ul>
              <li class="category-item">
                <div class="category-content">
                <router-link :to="{ name: 'Category', params: { categoryName: element.name, subcategoryName: subcategory.name } }">
                  <strong>{{ subcategory.name }}</strong>
                </router-link>
                <span v-if="editMode" class="edit-icon" @click="handleEditCategory(element)">
                  <img src="@/assets/pencil.svg" alt="pencil">
                </span>
              </div>
                <Draggable
                  v-model="subcategory.subsubcategories"
                  :group="{ name: 'subsubcategories' }"
                  item-key="name"
                  :disabled="!editMode">
                  <template #item="{ element: subsubcategory }">
                    <ul>
                      <li class="category-item">
                        <div class="category-content">
                        <router-link :to="{ name: 'Category', params: { categoryName: element.name, subcategoryName: subcategory.name, subsubcategoryName: subsubcategory.name } }">
                          <strong>{{ subsubcategory.name }}</strong>
                        </router-link>

                        <span v-if="editMode" class="edit-icon" @click="handleEditCategory(element)">
                          <img src="@/assets/pencil.svg" alt="pencil">
                        </span>
                      </div>
                        <Draggable
                          v-model="subsubcategory.products"
                          :group="{ name: 'products' }"
                          item-key="id"
                          :move="checkSameList"
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
                  :move="checkSameList" 
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
          :move="checkSameList" 
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

const checkSameList = (evt) => {
  return evt.from === evt.to;
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

.category-item {
  position: relative;
}

.category-content {
  display: flex;
  align-items: center;
}

.edit-icon {
  display: none; 
  cursor: pointer;
  margin-left: 8px;
}

.category-content:hover .edit-icon {
  display: inline-block; 
}

.edit-icon img {
  width: 16px; 
  height: 16px;
  stroke: #ccc; 
}

a {
  text-decoration: none;
  color: white;
}

</style>
