<template>
  <div v-if="show" class="image-gallery">
    <n-button 
      class="prev-button" 
      @click="prevImage" 
      :disabled="currentIndex === 0"
      style="color: white;">
      &#9664;
    </n-button>
    
    <div class="gallery-content">
      <img :src="currentImage" alt="gallery image" class="gallery-image" v-if="currentImage" />
      
      <n-button 
        class="close-button" 
        @click="$emit('close')" 
        style="color: white; font-size: 24px;">
        ×
      </n-button>
    </div>

    <n-button 
      class="next-button" 
      @click="nextImage" 
      :disabled="currentIndex >= images.length - 1"
      style="color: white;">
      &#9654;
    </n-button>
  </div>
</template>

<script setup>
import { computed, ref, defineProps } from 'vue';
import { NButton } from 'naive-ui';

const props = defineProps({
  images: {
    type: Array,
    required: true
  },
  show: {
    type: Boolean,
    required: true
  }
});

const currentIndex = ref(0);

const currentImage = computed(() => {
  return props.images[currentIndex.value] ? props.images[currentIndex.value] : null; 
});

const nextImage = () => {
  if (currentIndex.value < props.images.length - 1) {
    currentIndex.value++;
  }
};

const prevImage = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
  }
};
</script>

<style scoped>
.image-gallery {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.gallery-content {
  text-align: center;
  display: flex;
  width: 400px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}

.gallery-image {
  max-width: 80%;
  max-height: 80%;
}

.prev-button, .next-button {
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
  z-index: 101;
  background: transparent;
  border: none;
  font-size: 36px;
}

.prev-button {
  left: 20px;
}

.next-button {
  right: 20px;
}

.close-button {
  position: fixed;
  top: 10px;
  right: 10px;
  background: transparent;
  font-size: 36px;
}
</style>
