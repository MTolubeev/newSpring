<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button @click="emitClose" class="close-button">✖</button>
      <form @submit.prevent="uploadFile">
        <div class="form-group">
          <label>Изображение к товару:</label>
          <input type="file" @change="handleFileChange" />
        </div>

        <div class="form-group">
          <label>Название товара:</label>
          <input
            type="text"
            v-model="product.title"
            placeholder="Название"
            required
          />
        </div>

        <div class="form-group">
          <label>Цена:</label>
          <input
            type="number"
            placeholder="Цена"
            v-model.number="product.price"
            required
          />
        </div>

        <div class="form-group">
          <label>Скидочная цена:</label>
          <input
            type="number"
            placeholder="Скидочная цена"
            v-model="product.discountPrice"
          />
        </div>

        <div class="form-group">
          <label>Описание товара:</label>
          <textarea v-model="product.description" required></textarea>
        </div>

        <div class="form-group">
          <label>Категория товара:</label>
          <input
            type="text"
            placeholder="Категория"
            v-model="product.category"
            required
          />
        </div>

        <div class="form-group">
          <label>Количество товаров:</label>
          <input
            type="number"
            placeholder="Количество"
            v-model="product.count"
          />
        </div>

        <button class="create__product" type="submit">Создать товар</button>
      </form>
      <p v-if="message">{{ message }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue";
import axios from "axios";

const emit = defineEmits(["close"]);

const emitClose = () => {
  emit("close");
};

const file = ref(null);
const product = ref({
  title: "",
  price: "",
  discountPrice: "",
  description: "",
  category: "",
  count: "",
});
const message = ref("");

function handleFileChange(event) {
  file.value = event.target.files[0];
}

async function uploadFile() {
  if (file.value && product.value.title && product.value.price) {
    const formData = new FormData();
    formData.append("file1", file.value);
    formData.append("title", product.value.title);
    formData.append("price", product.value.price);
    formData.append("discountPrice", product.value.discountPrice);
    formData.append("description", product.value.description);
    formData.append("category", product.value.category);
    formData.append("count", product.value.count);

    try {
      const response = await axios.post(
        "http://localhost:8080/product/create",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${getToken()}`,
          },
        }
      );
      console.log(response.data);
      message.value = "Ваш продукт создан!";
    } catch (error) {
      console.error("Error uploading file:", error.response?.data || error);
      message.value = "Неудалось создать продукт";
    }
  } else {
    message.value = "Заполните все поля!";
  }
}

function getToken() {
  return localStorage.getItem("authToken") || "";
}
</script>

<style scoped>
.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
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
  height: 150px;
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
