<template>
  <div class="title__comments">
    <h2>Отзывы о товаре</h2>
    <div class="controls">
      <h3
        v-if="comments.length > 3"
        class="open__comments"
        @click="toggleComments"
      >
        {{ showAll ? 'Скрыть отзывы' : 'Показать все отзывы' }}
      </h3>
      <n-button
        type="success"
        @click="handleWriteReview"
        class="write-review-btn"
      >
        Написать отзыв
      </n-button>
      <n-select
        v-if="comments.length > 0"
        :options="sortOptions"
        v-model:value="sortOrder"
        placeholder="Сортировать по"
        style="width: 200px; margin-left: 20px;"
      />
    </div>
  </div>

  <div class="comments__wrapper">
    <div v-if="comments.length > 0" class="comments__items">
      <n-card v-for="comment in sortedComments" :key="comment.id">
        <div class="profile">
          <img src="@/assets/Union.svg" alt="user" />
          <strong>{{ comment.username }}</strong>
          <div class="stars">
            <svg
              v-for="star in maxStars"
              :key="star"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              :fill="star <= comment.score ? 'gold' : 'lightgray'"
              width="20"
              height="20"
            >
              <path
                d="M12 2l3.09 6.26L22 9.27l-5 4.87L18.18 21 12 17.27 5.82 21 7 14.14 2 9.27l6.91-1.01L12 2z"
              />
            </svg>
          </div>
        </div>
        <div>{{ comment.text }}</div>
      </n-card>
    </div>
    <div v-else>
      <h3>Комментариев пока что нет</h3>
    </div>
  </div>
  <WriteReviewModal v-model:show="showReviewModal" :productId="productId" />
</template>

<script setup>
import { defineProps, ref, computed, onMounted } from 'vue';
import { NCard, NButton, NSelect } from 'naive-ui';
import WriteReviewModal from './WriteReviewModal.vue';
import { useUserStore } from '@/store/userStore';

const userStore = useUserStore();
const user = computed(() => userStore.user.value);

const maxStars = 5;

const props = defineProps({
  comments: {
    type: Array,
    required: true
  },
  productId: {
    type: Number,
    required: true
  }
});

const showAll = ref(false);
const showReviewModal = ref(false);
const sortOrder = ref('desc'); 


const sortOptions = [
  { label: 'От высоких к низким', value: 'desc' },
  { label: 'От низких к высоким', value: 'asc' }
];


const toggleComments = () => {
  showAll.value = !showAll.value;
};


const sortedComments = computed(() => {
  const sorted = [...props.comments];
  sorted.sort((a, b) => {
    return sortOrder.value === 'asc' ? a.score - b.score : b.score - a.score;
  });
  return showAll.value ? sorted : sorted.slice(0, 3);
});

const handleWriteReview = () => {
  if (user.value) {
    showReviewModal.value = true; 
  } else {
    alert('Вы не авторизованы. Пожалуйста, войдите, чтобы оставить отзыв.'); 
  }
};

onMounted(async () => {
  await userStore.fetchUser(); 
});
</script>

<style scoped>
.comments__wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}
.comments__items {
  margin-top: 30px;
  display: grid;
  grid-template-columns: repeat(3, 1fr); 
  gap: 30px;
  margin-top: 30px;
}
.profile {
  display: flex;
  gap: 10px;
}
.title__comments {
  display: flex;
  margin-top: 20px;
  align-items: center;
  flex-direction: column;
  justify-content: center;
}
h2, h3 {
  margin: 0;
  cursor: pointer;
}
.controls {
  display: flex;
  align-items: center;
  gap: 15px;
}

.n-card {
  width: 300px;
  border-radius: 20px;
}
</style>
