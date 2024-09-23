<template>
  <div>
    <label>{{ label }}</label>
    <div style="display: flex; align-items: center">
      <NSelect v-model:value="selectedValue" :options="options" />
      <NButton @click="toggleInput" size="small" style="margin-left: 8px">
        {{ showInput ? "-" : "+" }}
      </NButton>
    </div>

    <NInput
      v-if="showInput"
      :value="inputValue"
      @update:value="updateInputValue"
      placeholder="Введите новое значение"
      style="margin-top: 8px"
    />
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from "vue";
import { NSelect, NButton, NInput } from "naive-ui";

defineProps({
  options: {
    type: Array,
    default: () => [],
  },
  label: String,
});

const emit = defineEmits(["data-changed"]);
const selectedValue = ref(null);
const inputValue = ref("");
const showInput = ref(false);


const updateInputValue = (value) => {
  inputValue.value = value;
};
const toggleInput = () => {
  showInput.value = !showInput.value;
  if (showInput.value) {
    selectedValue.value = null;
    inputValue.value = ""; 
  }
};

watch(selectedValue, (newValue) => {
  if (newValue !== null) {
    showInput.value = false;
    emit("data-changed", newValue);
    inputValue.value = "";
  }
});

watch(inputValue, (newValue) => {
  if (showInput.value && newValue !== "") {
    emit("data-changed", newValue);
  }
});
</script>
