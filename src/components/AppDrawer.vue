<template>
  <div v-if="isVisible" class="catalog">
    <div class="cart">
      <h2>Каталог товаров</h2>
      <svg
        @click="$emit('close-drawer')"
        width="16"
        height="14"
        viewBox="0 0 16 14"
        fill="#fff"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M1 7H14.7143"
          stroke="#fff"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
        <path
          d="M8.71436 1L14.7144 7L8.71436 13"
          stroke="#fff"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
      <button @click="saveOrder" class="save-button">Сохранить</button>
    </div>

    <!-- Drag and Drop для категорий -->
    <Draggable v-model="categories" :group="{ name: 'categories' }" @end="onDragEnd" item-key="name">
      <template #item="{ element }">
        <li>
          <router-link :to="{ name: 'Category', params: { categoryName: element.name } }">
            <strong>{{ element.name }}</strong>
          </router-link>

          <!-- Drag and Drop для подкатегорий -->
          <Draggable v-model="element.subcategories" :group="{ name: 'subcategories' }" item-key="name">
            <template #item="{ element: subcategory }">
              <ul>
                <li>
                  <router-link
                    :to="{
                      name: 'Category',
                      params: {
                        categoryName: element.name,
                        subcategoryName: subcategory.name,
                      },
                    }"
                  >
                    <strong>{{ subcategory.name }}</strong>
                  </router-link>

                  <Draggable v-model="subcategory.subsubcategories" :group="{ name: 'subsubcategories' }" item-key="name">
                    <template #item="{ element: subsubcategory }">
                      <ul>
                        <li>
                          <router-link
                            :to="{
                              name: 'Category',
                              params: {
                                categoryName: element.name,
                                subcategoryName: subcategory.name,
                                subsubcategoryName: subsubcategory.name,
                              },
                            }"
                          >
                            <strong>{{ subsubcategory.name }}</strong>
                          </router-link>

                          <!-- Drag and Drop для продуктов в под-подкатегории -->
                          <Draggable v-model="subsubcategory.products" :group="{ name: 'products' }" item-key="id">
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

                  <!-- Drag and Drop для продуктов в подкатегории -->
                  <Draggable v-model="subcategory.products" :group="{ name: 'products' }" item-key="id">
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

          <!-- Drag and Drop для продуктов без подкатегории -->
          <Draggable v-model="element.productsWithoutSubcategory" :group="{ name: 'products' }" item-key="id">
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
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue';
import axios from 'axios';
import Draggable from 'vuedraggable'; // Импортируем draggable
import { useOrganizeProducts } from '@/composables/useOrganizeProducts';

const categories = ref([]);
const { organizeProductsByCategories } = useOrganizeProducts();

defineProps({
  isVisible: Boolean
});

const fetchData = async () => {
  try {
    const response = await axios.get('http://localhost:8082/products');
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

// Метод для обработки завершения перетаскивания
const onDragEnd = (event) => {
  console.log('Элемент был перемещен:', event);
};

// Метод для сохранения обновленного порядка
const saveOrder = async () => {
  try {
    // Создание запроса с обновленными данными
    const updatedCategories = categories.value.map((category, categoryIndex) => {
      // Обновление порядка категорий
      category.order = categoryIndex + 1;
      category.subcategories.forEach((subcategory, subcategoryIndex) => {
        // Обновление порядка подкатегорий
        subcategory.subcategoryOrder = subcategoryIndex + 1;
        subcategory.subsubcategories.forEach((subsubcategory, subsubcategoryIndex) => {
          // Обновление порядка под-подкатегорий
          subsubcategory.subsubcategoryOrder = subsubcategoryIndex + 1;
          subsubcategory.products.forEach((product, productIndex) => {
            // Обновление порядка продуктов
            product.order = productIndex + 1;
          });
        });
      });
      return category;
    });

    // Отправка обновленных данных на сервер
    await axios.put('http://localhost:8082/products', { products: updatedCategories }, {
  headers: {
    'Content-Type': 'application/json'
  }
});
    console.log('Обновленный порядок сохранен успешно.');
  } catch (error) {
    console.error('Ошибка при сохранении порядка:', error);
  }
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
/* Ваши стили остаются без изменений */
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
.catalog a {
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
