<template>
    <div class="cRTT">
    <MyHeader @toggle-drawer="toggleDrawer" />
    <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
    <MyCart></MyCart>
</div>
</template>

<script setup>
import MyCart from '@/components/MyCart.vue';
import MyHeader from '@/components/MyHeader.vue';
import MyDrawer from '@/components/MyDrawer.vue';
import { useDrawer } from '@/composables/useHeader.js';

const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();


</script>

<style scoped>
.cRTT{
    height: 100vh;
}

</style>