<template>
  <n-card size="huge" class="card" @click="navigateToproduct" hoverable>
    <div class="edit_container">
        <img src="@/assets/pencil.svg" alt="edit_product" @click.stop="editModel">
      </div>
    <div class="card-content" v-if="!isEdited">
      <div class="image-container">
        <img :src="item.imageUrl" alt="png" />
      </div>
      <h3>{{ item.title }}</h3>
      <div class="info_card">
        <span v-if="isAuthenticated">
          Цена: <b>{{ item.discountPrice }} руб.</b>
          <del style="margin-left: 10px">{{ item.price }} руб.</del>
        </span>
        <span v-else>
          Цена: <b>{{ item.price }} руб.</b>
        </span>
        <span>
          Количество товаров осталось: <b>{{ item.count }}</b>
        </span>
      </div>
    <div class="card__button">
      <CartButton :productId="item.id" :product="item" @click.stop />
      <n-button style="--n-border-hover: 1px solid #3B5998; --n-text-color-hover:#3B5998;"  
      v-if="isAdmin" 
      @click.stop="openConfirmDialog">
        Удалить товар из списка
      </n-button>
    </div>
  </div>
  <div class="card-content" v-else>
      <img style="width: 70px;" v-if="!imageDeleted" :src="item.imageUrl" alt="png" @click.stop="deleteImage" />
      <input type="file" v-else @change="handleImageChange" />
    <label>Название товара</label>
      <n-input v-model:value="editProduct.title" placeholder="Название товара"/>
    <label>Описание товара</label>
      <n-input type="textarea" v-model:value="editProduct.description" placeholder="Введите описание товара"/>
    <label>Цена товара</label>
      <n-input v-model:value="editProduct.price" placeholder="Введите цену товара"/>
    <label>Скидочная цена товара</label>
      <n-input v-model:value="editProduct.discountPrice" placeholder="Введите скидку товара"/>
    <label>Количество товаров</label>
      <n-input v-model:value="editProduct.count" placeholder="Введите скидку товара"/>

    <div class="card__button">
      <n-button type="success" @click.stop="editProductPut">Сохранить изменения</n-button>
      <n-button type="error" @click.stop="cancelEdit">Отменить изменения</n-button>
    </div>
  </div>
  </n-card>

  <div v-if="confirmDialogVisible" class="dialog-overlay">
    <n-dialog
      class="confirm-dialog"
      title="Подтверждение удаления"
      positive-text="Удалить"
      negative-text="Отмена"
      @positive-click="deleteProduct"
      @negative-click="closeConfirmDialog"
      :closable="false">
      Вы уверены, что хотите удалить этот продукт?
    </n-dialog>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { NCard, NButton, NDialog, NInput } from "naive-ui";
import { useUserStore } from "@/store/userStore";
import CartButton from "./BasketButton.vue";
import axios from "axios";

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["delete"]);

const userStore = useUserStore();
const router = useRouter();
const confirmDialogVisible = ref(false);
const isAuthenticated = ref(false);
const isEdited = ref(false);
const imageDeleted = ref(false);
const editProduct = ref({...props.item});

const role = computed(() => userStore.role.value);
const isAdmin = computed(() => role.value === "ROLE_ADMIN");

const checkAuth = () => {
  const token = localStorage.getItem("token");
  if (token) {
    isAuthenticated.value = true;
  }
};

const editModel = () =>{
  isEdited.value = true;
}

const deleteImage = () => {
  imageDeleted.value = true;
};

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      editProduct.value.imageUrl = e.target.result;
      imageDeleted.value = false;
    };
    reader.readAsDataURL(file);
  }
};

const cancelEdit = () => {
  editProduct.value = { ...props.item };
  isEdited.value = false;
  imageDeleted.value = false;
};
const editProductPut = () =>{
  try{
    console.log(editProduct.value);
  } catch(error){
    console.log(error);
  }
}



const deleteProduct = async () => {
  try {
    await axios.post(`http://localhost:8080/product/delete/${props.item.id}`);
    closeConfirmDialog();
    emit("delete", props.item.id);
  } catch (error) {
    console.error("Ошибка при удалении продукта", error);
    closeConfirmDialog();
  }
};

const openConfirmDialog = (event) => {
  event.stopPropagation();
  confirmDialogVisible.value = true;
};
const closeConfirmDialog = () => {
  confirmDialogVisible.value = false;
};


const navigateToproduct = () => {
  router.push({ path: `/product-view/${props.item.id}` });
};

onMounted(() => {
  userStore.fetchUser();
  checkAuth();
});
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

.card-content {
  display: flex;
  flex-direction: column;
}
.image-container {
  width: 300px;
  height: 300px;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
.n-button {
  width: 100%;
}
.card__button {
  margin-top: 20px;
}
.card h3 {
  font-size: 24px;
  margin: 0 0 10px 0;
}
.card p {
  font-size: 16px;
  margin: 0 0 20px 0;
}
.info_card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}
.info_card span {
  font-size: 16px;
}
.info_card b {
  font-weight: bold;
}
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9998;
}

.confirm-dialog {
  z-index: 9999;
}
.edit_container {
  position: absolute;
  top: 40px;
  right: 40px;
}
.edit_container img{
  width: 20px;
}
</style>
