<template>
  <div v-if="show" class="image-gallery">
    <n-button 
      class="prev-button" 
      :disabled="currentIndex === 0"
      style="color: white;"
      @click="prevImage">
      &#9664;
    </n-button>
    
    <div class="gallery-content">
      <img 
        v-if="currentImage"
        :src="currentImage"
        class="gallery-image" 
        alt="gallery image"/>
      <n-button 
        class="close-button" 
        style="color: white; font-size: 24px;"
        @click="emitClose">
        ×
      </n-button>

      <n-button 
        class="delete-button" 
        style="color: white; font-size: 24px;"
        @click="deleteImage">
        🗑
      </n-button>
    </div>

    <n-button 
      class="next-button" 
      :disabled="currentIndex >= images.length - 1"
      style="color: white;"
      @click="nextImage">
      &#9654;
    </n-button>
  </div>
</template>

<script setup>
import { computed, ref, defineProps, defineEmits } from 'vue';
import { NButton } from 'naive-ui';

const props = defineProps({
  images: {
    type: Array,
    required: true
  },
  show: {
    type: Boolean,
    required: true
  },
  commentId:{
    type: Number,
    required: true
  }
});

const emit = defineEmits(['close', 'delete-image']);

const currentIndex = ref(0);

const currentImage = computed(() => {
  return props.images[currentIndex.value] ? props.images[currentIndex.value].base64 : null;
});

const deleteImage = () => {
  if (currentImage.value) {
    const imageId = props.images[currentIndex.value].id;
    emit('delete-image', { imageId, commentId: props.commentId });
  }
};

const emitClose = () => {
  emit('close');
};

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

.close-button, .delete-button {
  position: fixed;
  top: 10px;
  background: transparent;
  font-size: 36px;
}

.close-button {
  right: 80px;
}

.delete-button {
  right: 10px;
}
</style>
