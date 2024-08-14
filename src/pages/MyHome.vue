<template>
  <MyHeader @toggle-drawer="toggleDrawer" />
  <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
    <MyCardList />

</template>

<script setup>
import MyHeader from "@/components/MyHeader.vue";
import MyDrawer from "@/components/MyDrawer.vue";
import MyCardList from "@/components/MyCardList.vue";
import { useDrawer } from '@/composables/useHeader.js';

const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();


</script>


<style scoped>

</style>
