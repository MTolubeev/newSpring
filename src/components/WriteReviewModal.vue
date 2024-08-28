<template>
    <n-modal v-model:show="visible" title="Написать отзыв" @close="handleClose" content-style="padding:20px">
      <div class="review-form">
        <label for="reviewText">Ваш отзыв:</label>
        <textarea id="reviewText" v-model="reviewText" rows="4"></textarea>
  
        <label>Оценка:</label>
        <div class="stars">
          <svg
            v-for="star in maxStars"
            :key="star"
            @click="setRating(star)"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            :fill="star <= rating ? 'gold' : 'lightgray'"
            width="30"
            height="30">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87L18.18 21 12 17.27 5.82 21 7 14.14 2 9.27l6.91-1.01L12 2z"/>
          </svg>
        </div>
  
        <label for="uploadImage">Добавить изображение:</label>
        <input type="file" id="uploadImage" @change="handleImageUpload" accept="image/*">
  
        <button @click="submitReview" class="submit-btn">Отправить отзыв</button>
      </div>
    </n-modal>
  </template>
  
  <script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import axios from 'axios';
import { NModal } from 'naive-ui';

const props = defineProps({
  modelValue: Boolean,
  productId: {
    type: Number,
    required: true
  }
});

const emits = defineEmits(['update:modelValue']);

const visible = ref(props.modelValue);
const reviewText = ref('');
const rating = ref(0);
const maxStars = 5;
const image = ref(null);

const setRating = (star) => {
  rating.value = star;
};

const handleImageUpload = (event) => {
  image.value = event.target.files[0];
};

const handleClose = () => {
  emits('update:modelValue', false);
};

const submitReview = async () => {
  if (!reviewText.value || rating.value === 0) {
    alert('Пожалуйста, заполните все поля.');
    return;
  }

  const formData = new FormData();
  formData.append('productId', props.productId); 
  formData.append('text', reviewText.value);
  formData.append('score', rating.value.toString());
  if (image.value) {
    formData.append('image', image.value);
  }
  
  console.log("Submitting review with data:", {
    productId: props.productId,
    text: reviewText.value,
    score: rating.value,
    image: image.value,
  }); 

  const token = localStorage.getItem('token'); 

  try {
    const response = await axios.post('http://localhost:8080/comments/add', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': token,
      },
    });
    console.log('Комментарий успешно отправлен:', response.data);
  } catch (error) {
    console.error('Ошибка при отправке комментария:', error.response ? error.response.data : error.message);
  }
};

watch(() => props.modelValue, (newVal) => {
  visible.value = newVal;
});
</script>

  <style scoped>
  .n-modal  {
  padding: 20px;
  box-sizing: border-box; 
}
  .n-modal{
    background-color: #fff;
  }
  .review-form {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .stars {
    display: flex;
    gap: 5px;
    cursor: pointer;
  }
  
  .submit-btn {
    padding: 8px 16px;
    background-color: #28a745;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .submit-btn:hover {
    background-color: #218838;
  }
  </style>
  