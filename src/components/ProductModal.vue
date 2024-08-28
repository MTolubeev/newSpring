<template>
  <n-card content-style="display: flex; flex-direction: column; gap: 10px;">
    <h3>Вы точно хотите удалить товар?</h3>
    <div class="confirmation-buttons">
      <n-button type="error" @click="confirm">Удалить</n-button>
      <n-button type="warning" @click="cancel">Отмена</n-button>
    </div>
  </n-card>
</template>

<script setup>
import { defineEmits, defineProps } from "vue";
import { NButton, NCard } from "naive-ui";

const props = defineProps({
  onConfirm: {
    type: Function,
    required: true,
  },
  onCancel: {
    type: Function,
    required: true,
  },
});

const emit = defineEmits();

const confirm = () => {
  emit("confirm");
  props.onConfirm();
};

const cancel = () => {
  emit("cancel");
  props.onCancel();
};
</script>

<style scoped>
.confirmation-buttons {
  display: flex;
  gap: 10px;
}
</style>
