<template>
  <div v-if="isVisible" class="catalog">
    <div class="cart">
      <h2>Каталог товаров</h2>
      <svg @click="$emit('close-drawer')" width="16" height="14" fill="#fff">
        <path d="M1 7H14.7143" stroke="#fff" stroke-width="2" />
        <path
          d="M8.71436 1L14.7144 7L8.71436 13"
          stroke="#fff"
          stroke-width="2"
        />
      </svg>
    </div>

    <n-button v-if="!editMode && isAdmin" @click="enableEditMode">
      Включить режим редактирования
    </n-button>

    <Draggable
      v-model="categories"
      :group="{ name: 'categories' }"
      @end="onDragEnd"
      item-key="name"
      :disabled="!editMode">
      <template #item="{ element }">
        <CategoryItem :category="element" :editMode="editMode" />
      </template>
    </Draggable>

    <button v-if="editMode" @click="saveOrder">Сохранить изменения</button>
    <button v-if="editMode" @click="cancelEditMode">Отменить изменения</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, defineProps } from "vue";
import axios from "axios";
import Draggable from "vuedraggable";
import { NButton } from "naive-ui";
import { useUserStore } from "@/store/userStore";
import { useOrganizeProducts } from "@/composables/useOrganizeProducts";
import CategoryItem from "./CategoryItem.vue";

const userStore = useUserStore();
const categories = ref([]);
const editMode = ref(false);
const { organizeProductsByCategories } = useOrganizeProducts();

defineProps({
  isVisible: Boolean,
});

const enableEditMode = () => {
  editMode.value = true;
};
const cancelEditMode = () => {
  editMode.value = false;
};
const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/product/getAll");
    const products = response.data;
    if (Array.isArray(products)) {
      categories.value = organizeProductsByCategories(products);
    } else {
      console.error("Неправильный формат данных:", products);
    }
  } catch (error) {
    console.error("Ошибка при получении данных:", error);
  }
};

const collectChanges = () => {
  let changes = {};

  const processCategory = (category, categoryIndex) => {
    category.productsWithoutSubcategory.forEach((product, productIndex) => {
      if (!changes[product.id]) {
        changes[product.id] = [];
      }
      changes[product.id].push({
        name: category.name,
        subcategory: null,
        subsubcategory: null,
        order: categoryIndex + 1,
        subcategoryOrder: null,
        subsubcategoryOrder: null,
        productOrder: productIndex + 1,
      });
    });

    category.subcategories.forEach((subcategory, subcategoryIndex) => {
      subcategory.products.forEach((product, productIndex) => {
        if (!changes[product.id]) {
          changes[product.id] = [];
        }
        changes[product.id].push({
          name: category.name,
          subcategory: subcategory.name,
          subsubcategory: null,
          order: categoryIndex + 1,
          subcategoryOrder: subcategoryIndex + 1,
          subsubcategoryOrder: null,
          productOrder: productIndex + 1,
        });
      });

      subcategory.subsubcategories.forEach(
        (subsubcategory, subsubcategoryIndex) => {
          subsubcategory.products.forEach((product, productIndex) => {
            if (!changes[product.id]) {
              changes[product.id] = [];
            }
            changes[product.id].push({
              name: category.name,
              subcategory: subcategory.name,
              subsubcategory: subsubcategory.name,
              order: categoryIndex + 1,
              subcategoryOrder: subcategoryIndex + 1,
              subsubcategoryOrder: subsubcategoryIndex + 1,
              productOrder: productIndex + 1,
            });
          });
        }
      );
    });
  };

  categories.value.forEach((category, index) => {
    processCategory(category, index);
  });

  return changes;
};

const saveOrder = async () => {
  try {
    const changes = collectChanges();
    const requests = Object.keys(changes).map((productId) =>
      axios.put("http://localhost:8080/product/categories/reorder", {
        [productId]: changes[productId],
      })
    );
    await Promise.all(requests);

    editMode.value = false;
  } catch (error) {
    console.error("Ошибка при сохранении порядка:", error);
  }
};

const role = computed(() => userStore.role.value);
const isAdmin = computed(() => role.value === "ROLE_ADMIN");

onMounted(() => {
  userStore.fetchUser();
  fetchData();
});
</script>

<style scoped>
.catalog {
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100%;
  width: 384px;
  background-color: #465a86;
  padding: 28px 40px;
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

ul,
li {
  padding: 0;
  margin: 0;
  list-style: none;
}
a:focus {
  outline: none;
}
li {
  margin-bottom: 8px;
  padding: 8px;
  border-radius: 4px;
}
ul {
  padding-left: 16px;
}
</style>
