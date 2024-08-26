<template>
  <div class="modal-overlay">
    <div class="modal-content">
    <form>
        <input type="text">
        <input type="text">
        <button type="button" @click="emitClose">закрыть</button>
    </form>
  </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['close']);

const emitClose = () => {
 
  emit('close');
};
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
  width: 300px;
  z-index: 100;
}

</style>