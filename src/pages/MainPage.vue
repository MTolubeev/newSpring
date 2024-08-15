<template>
  <MyHeader @toggle-drawer="toggleDrawer" />
  <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
    <MyCardList />

</template>

<script setup>
import MyHeader from "@/components/AppHeader.vue";
import MyDrawer from "@/components/AppDrawer.vue";
import MyCardList from "@/components/CardList.vue";
import { useDrawer } from '@/composables/useHeader.js';

const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();


</script>


<style scoped>

</style>
