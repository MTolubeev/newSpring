<template>
    <div class="cRTT">
    <MyHeader @toggle-drawer="toggleDrawer" />
    <MyDrawer :isVisible="isDrawerVisible" @close-drawer="closeDrawer" />
    <AppCart/>
</div>
</template>

<script setup>
import AppCart from '@/components/AppCart.vue';
import MyHeader from '@/components/AppHeader.vue';
import MyDrawer from '@/components/AppDrawer.vue';
import { useDrawer } from '@/composables/useHeader.js';

const { isDrawerVisible, toggleDrawer, closeDrawer } = useDrawer();


</script>

<style scoped>
.cRTT{
    height: 100vh;
}

</style>