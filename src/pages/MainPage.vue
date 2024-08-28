<template>
  <MyHeader @toggle-drawer="toggleDrawer" />
  <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
  <n-button v-if="isAdmin" @click="openModal" class="button__add" type="warning">Добавить новый товар</n-button>
  <AddProduct v-if="showModal" @close="closeModal" /> 
    <MyCardList />
  
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { NButton } from 'naive-ui';
import MyHeader from "@/components/AppHeader.vue";
import MyDrawer from "@/components/AppDrawer.vue";
import AddProduct from '@/components/AddProduct.vue';
import MyCardList from "@/components/CardList.vue";
import { useUserStore } from "@/store/userStore";
import { useDrawer } from '@/composables/useHeader.js';
const userStore = useUserStore();
const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();


const showModal = ref(false);

const openModal = () =>{
  showModal.value = true;
  document.body.style.overflow = 'hidden';
}
 const closeModal = () => {
  showModal.value = false;
  document.body.style.overflow = '';
};

const role = computed(() => userStore.role.value);
const isAdmin = computed(() => role.value === 'ROLE_ADMIN')


onMounted(async () => {
    await userStore.fetchUser();
});
</script>


<style scoped>
.button__add{
  position: relative;
  left: 10%;
  top: 60px;
}
</style>
