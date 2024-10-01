<template>
    <n-card class="card">
        <div class="correct_img">
      <img
        style="width: 70px"
        v-if="!imageDeleted"
        :src="item.imageUrl"
        alt="png"
        @click.stop="deleteImage"/>
      <n-upload
        :default-file-list="fileList"
        list-type="image"
        :create-thumbnail-url="createThumbnailUrl"
        v-else
        @change="handleChange"
        max="1"
      >
        <n-button>Upload</n-button>
      </n-upload>
    </div>
      <label>Название товара</label>
      <n-input v-model:value="editProduct.title" placeholder="Название товара" />
      <label>Описание товара</label>
      <n-input
        type="textarea"
        v-model:value="editProduct.description"
        placeholder="Введите описание товара"
      />
      <label>Цена товара</label>
      <n-input
        v-model:value="editProduct.price"
        placeholder="Введите цену товара"
      />
      <label>Скидочная цена товара</label>
      <n-input
        v-model:value="editProduct.discountPrice"
        placeholder="Введите скидку товара"
      />
      <label>Количество товаров</label>
      <n-input
        v-model:value="editProduct.count"
        placeholder="Введите скидку товара"
      />
  
      <label>Категория</label>
      <n-select
        v-model:value="selectedCategory"
        :options="categoryOptions"
        placeholder="Выберите категорию"
        @update:value="handleCategoryChange"
      />
  
      <label>Подкатегория</label>
      <n-select
        v-model:value="selectedSubcategory"
        :options="subcategoryOptions"
        placeholder="Выберите подкатегорию"
        @update:value="handleSubcategoryChange"
      />
  
      <label>Субсубкатегория</label>
      <n-select
        v-model:value="selectedSubsubcategory"
        :options="subsubcategoryOptions"
        placeholder="Выберите субсубкатегорию"
      />
  
      <div class="card__button">
        <n-button type="success" @click.stop="saveChanges">
          Сохранить изменения
        </n-button>
        <n-button type="error" @click.stop="cancelEdit">
          Отменить изменения
        </n-button>
      </div>
    </n-card>
  </template>
  
  <script setup>
  import { ref, defineProps, defineEmits } from "vue";
  import { NCard, NButton, NInput, NUpload, NSelect } from "naive-ui";
  
  const props = defineProps({
    item: {
      type: Object,
      required: true,
    },
    categoryOptions: {
      type: Array,
      required: true,
    },
    subcategoryOptions: {
      type: Array,
      required: true,
    },
    subsubcategoryOptions: {
      type: Array,
      required: true,
    },
  });
  
  const emit = defineEmits(["save", "cancel"]);
  
  const imageDeleted = ref(false);
  const editProduct = ref({ ...props.item });
  const fileList = ref([]);
  
  const selectedCategory = ref(props.item.categories[0]?.name || '');
  const selectedSubcategory = ref(props.item.categories[0]?.subcategory || '');
  const selectedSubsubcategory = ref(props.item.categories[0]?.subsubcategory || '');
  
  const deleteImage = () => {
    imageDeleted.value = true;
    editProduct.value.imageUrl = null;
  };
  
  const handleChange = (event) => {
    if (event.fileList && event.fileList.length > 0) {
      const file = event.fileList[0].file;
      const reader = new FileReader();
      reader.onload = (e) => {
        editProduct.value.base64Image = e.target.result;
        fileList.value = [
          {
            id: file.uid,
            name: file.name,
            url: e.target.result,
          },
        ];
      };
      reader.readAsDataURL(file);
    }
  };
  
  const cancelEdit = () => {
    emit("cancel");
  };
  
const saveChanges = () => {
    delete editProduct.value.imageUrl;
    emit("save", {
      ...editProduct.value,
      categories: [
        {
          name: selectedCategory.value,
          subcategory: selectedSubcategory.value,
          subsubcategory: selectedSubsubcategory.value,
        },
      ],
    });
  };
  
  const handleCategoryChange = (value) => {
    selectedCategory.value = value;
    selectedSubcategory.value = '';
    selectedSubsubcategory.value = '';
  };
  
  const handleSubcategoryChange = (value) => {
    selectedSubcategory.value = value;
    selectedSubsubcategory.value = '';
  };
  </script>


<style scoped>
.card {
  width: 800px;
  padding: 50px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  cursor: pointer;
}
</style>