<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button @click="emitClose" class="close-button">✖</button>
      <form @submit.prevent="uploadFile">
        <div class="form-group">
          <label>Изображение к товару:</label>
          <input 
          type="file" 
          @change="handleFileChange" />
        </div>

        <div class="form-group">
          <label>Название товара:</label>
          <n-input
            type="text"
            v-model:value="product.title"
            placeholder="Название"
            required/>
        </div>

        <div class="form-group">
          <label>Цена:</label>
          <n-input
            type="number"
            placeholder="Цена"
            v-model:value="product.price"
            required/>
        </div>

        <div class="form-group">
          <label>Скидочная цена:</label>
          <n-input
            type="number"
            placeholder="Скидочная цена"
            v-model:value="product.discountPrice"/>
        </div>

        <div class="form-group">
          <label>Описание товара:</label>
          <n-input 
          type="textarea" 
          v-model:value="product.description" 
          required />
        </div>

        <div class="form-group">
          <SelectCategory
            :options="categoryOptions"
            label="Категория"
            @data-changed="(value) => handleDataChange('category')(value)"
          />
        </div>
        <div class="form-group">
          <SelectCategory
            :options="subcategoryOptions"
            label="Подкатегория (Необязательно)"
            @data-changed="(value) => handleDataChange('subcategory')(value)"
          />
        </div>
        <div class="form-group">
          <SelectCategory
            :options="subsubcategoryOptions"
            label="Подподкатегория (Необязательно)"
            @data-changed="(value) => handleDataChange('subsubcategory')(value)"
          />
        </div>
        <div class="form-group">
          <label>Количество товаров:</label>
          <n-input
            type="number"
            placeholder="Количество"
            v-model:value="product.count"
          />
        </div>
        <button class="create__product" type="submit">Создать товар</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, onMounted } from "vue";
import { NInput } from "naive-ui";
import axios from "axios";
import SelectCategory from "./SelectCategory.vue";
import { useNotificationService } from '@/composables/notificationUtils';

const { showNotificationMessage } = useNotificationService();
const emit = defineEmits(["close"]);

const file = ref(null);
const product = ref({
  title: "",
  price: "",
  discountPrice: "",
  description: "",
  category: "",
  count: "",
});
const categoryOptions = ref([]);
const subcategoryOptions = ref([]);
const subsubcategoryOptions = ref([]);
const selectedData = ref({
  category: null,
  subcategory: null,
  subsubcategory: null,
});

const emitClose = () => {
  emit("close");
};

const getUniqueValues = (items, key) => {
  return Array.from(new Set(items.map((item) => item[key])))
    .filter((value) => value !== null)
    .map((value) => ({
      label: value,
      value: value,
    }));
};

const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/product/getAll");
    const allCategories = response.data.flatMap((product) => product.categories);
    categoryOptions.value = getUniqueValues(allCategories, "name");
    subcategoryOptions.value = getUniqueValues(allCategories.filter((cat) => cat.subcategory !== null),"subcategory");
    subsubcategoryOptions.value = getUniqueValues(allCategories.filter((cat) => cat.subsubcategory !== null),"subsubcategory");
  } catch (error) {
    console.error("Ошибка при загрузке данных:", error);
  }
};

const handleDataChange = (field) => (value) => {
  selectedData.value[field] = value;
};

const handleFileChange = (event) => {
  file.value = event.target.files[0];
}

const uploadFile =  async() => {
  if (file.value && product.value.title && product.value.price) {
    const formData = new FormData();
    formData.append("file1", file.value);
    formData.append("title", product.value.title);
    formData.append("price", product.value.price);
    formData.append("discountPrice", product.value.discountPrice);
    formData.append("description", product.value.description);
    formData.append("category", selectedData.value.category);
    formData.append("subcategory", selectedData.value.subcategory || "");
    formData.append("subsubcategory", selectedData.value.subsubcategory || "");
    formData.append("count", product.value.count);
    try {
      const token = localStorage.getItem("token");
       const response = await axios.post("http://localhost:8080/product/create", formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: token,
          },
        });
        console.log(response);
        if(response.status === 201){
          window.location.reload();
        } else{
          showNotificationMessage('error', 'Товар не удалось создать');
        }
    } catch (error) {
      console.error("Error uploading file:", error.response?.data || error);
    }
  }
}

onMounted(() => {
  fetchData();
})
</script>

<style scoped>
.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  overflow-y: auto;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 99;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 700px;
  position: relative;
  z-index: 100;
}
.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input,
textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

textarea {
  height: 50px;
  resize: vertical;
}
.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #333;
  position: absolute;
  top: 10px;
  right: 20px;
}
.create__product {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}
.close-button:hover {
  color: #f0f0f0;
}
</style>
