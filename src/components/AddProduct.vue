<template>
    <form>
        <input type="text">
        <input type="text">
        <button type="button" @click="emitClose">закрыть</button>
    </form>

</template>

<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['close']);

const emitClose = () => {
 
  emit('close');
};
</script>

<style scoped>


</style>