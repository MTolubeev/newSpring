<template>
  <div class="title__comments">
    <h2>Отзывы о товаре</h2>
    <div class="controls">
      <h3
        v-if="comments.length > 3"
        class="open__comments"
        @click="toggleComments">
        {{ showAll ? 'Скрыть отзывы' : 'Показать все отзывы' }}
      </h3>
      <n-button
        type="success"
        @click="handleWriteReview"
        class="write-review-btn">
        Написать отзыв
      </n-button>
      <n-select
        v-if="comments.length > 0"
        :options="sortOptions"
        v-model:value="sortOrder"
        placeholder="Сортировать по"
        style="width: 200px; margin-left: 20px;"/>
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
              height="20">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87L18.18 21 12 17.27 5.82 21 7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
          </div>
        </div>
        <div>{{ comment.text }}</div>
        <div class="comment-images" v-if="comment.images.length > 0">
          <div class="images-container">
            <div class="images-wrapper">
              <img 
                v-for="image in comment.images.slice(0, 3)" 
                :src="convertBase64(image.bytes)" 
                :key="image.id" 
                class="comment-image" 
                alt="изображение комментария" 
                @click="openImageGallery(comment.images)" />
            </div>
            <n-button 
              quaternary circle
              v-if="comment.images.length > 3" 
              @click="openImageGallery(comment.images)" 
              size="large"
              class="gallery-button">
              +
            </n-button>
          </div>
        </div>
        <ImageGallery 
        :images="currentImages" 
        :show="showImageGallery" 
        @close="closeImageGallery"
        @delete-image="(imageId) => handleDeleteImage(comment.id, imageId)" />

        <img 
          v-if="isAdmin || isOwner(comment)" 
          src="@/assets/bin.webp"
          alt="admin badge" 
          class="admin-icon"
          @click="openDeleteDialog(comment.id)"/>
      </n-card>
    </div>
    <div v-else>
      <h3>Комментариев пока что нет</h3>
    </div>
  </div>

  <div v-if="confirmDialogVisible" class="modal-overlay">
    <div class="modal-content">
      <h3>Подтверждение удаления</h3>
      <p>Вы уверены, что хотите удалить этот комментарий?</p>
      <div class="modal-actions">
        <n-button @click="confirmDeleteComment" type="primary">Удалить</n-button>
        <n-button @click="closeConfirmDialog" type="default">Отмена</n-button>
      </div>
    </div>
  </div>

  <WriteReviewModal v-model:show="showReviewModal" :productId="productId" />
</template>

<script setup>
import { defineProps, ref, computed, onMounted } from 'vue';
import { NCard, NButton, NSelect } from 'naive-ui';
import WriteReviewModal from './WriteReviewModal.vue';
import { useUserStore } from '@/store/userStore';
import axios from 'axios';
import ImageGallery from './ImageGallery.vue';
import { useNotificationService } from '@/composables/notificationUtils';

const { showNotificationMessage } = useNotificationService();

const userStore = useUserStore();
const user = computed(() => userStore.user.value);
const role = computed(() => userStore.role.value);
const isAdmin = computed(() => role.value === 'ROLE_ADMIN');

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
const confirmDialogVisible = ref(false);
const commentIdToDelete = ref(null);
const sortOrder = ref('desc');
const currentImages = ref([]); 
const showImageGallery = ref(false); 

const sortOptions = [
  { label: 'От высоких к низким', value: 'desc' },
  { label: 'От низких к высоким', value: 'asc' }
];

const toggleComments = () => {
  showAll.value = !showAll.value;
};
const closeImageGallery = () => {
  showImageGallery.value = false;
  currentImages.value = []; 
};

const convertBase64 = (base64String) => {
  return base64String ? `data:image/jpeg;base64,${base64String}` : '';
};

const isOwner = (comment) => {
  return user.value && user.value.id === comment.userId;
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
    showNotificationMessage('error', 'Ошибка добавления комментария', 'Авторизуйтесь для создания комментария');
  }
};

const openDeleteDialog = (commentId) => {
  commentIdToDelete.value = commentId;
  confirmDialogVisible.value = true;
};

const closeConfirmDialog = () => {
  confirmDialogVisible.value = false;
  commentIdToDelete.value = null;
};

const confirmDeleteComment = async () => {
  if (!commentIdToDelete.value) return;

  try {
    const url = isAdmin.value
      ? `http://localhost:8080/comments/${commentIdToDelete.value}`
      : `http://localhost:8080/comments/usersComment/${commentIdToDelete.value}`;

    const token = localStorage.getItem("token");
    const response = await axios.delete(url, {
      headers: {
        Authorization: token,
      },
    });

    if (response.status === 200) {
      localStorage.setItem('showDeleteSuccessNotification', 'true');
      window.location.reload(); 
    } else {
      showNotificationMessage('error', 'Ошибка', 'Произошла ошибка при удалении комментария.');
    }
  } catch (error) {
    if (error.response) {
      showNotificationMessage('error', 'Ошибка', `Ошибка: ${error.response.data.message || error.response.data}`);
    } else {
      showNotificationMessage('error', 'Ошибка', 'Произошла ошибка при удалении комментария.');
    }
  } finally {
    closeConfirmDialog();
  }
};
const handleDeleteImage = async (commentId, imageId) => {
  try {
    const token = localStorage.getItem('token');
    console.log('Отправка запроса на удаление изображения:', {
      commentId,
      imageId,
      headers: {
        Authorization: token,
      },
    });

    const response = await axios.delete(`http://localhost:8080/comments/deleteImage/${commentId}`, {
      headers: {
        Authorization: token,
      },
      params: {
        commentImageId: imageId,
      },
    });

    if (response.status === 200) {
      showNotificationMessage('success', 'Изображение успешно удалено.');
      // window.location.reload();
    } else {
      showNotificationMessage('error', 'Ошибка при удалении изображения.');
    }
  } catch (error) {
    console.error(error);
    showNotificationMessage('error', 'Произошла ошибка при удалении изображения.');
  }
}
const openImageGallery = (images) => { 
  currentImages.value = images.map(image => ({
    base64: convertBase64(image.bytes),
    ...image
  }));
  showImageGallery.value = true; 
};

onMounted(async () => {
  await userStore.fetchUser();
  const notificationFlag = localStorage.getItem('showDeleteSuccessNotification');
  if (notificationFlag === 'true') {
    showNotificationMessage('success', 'Комментарий успешно удалён.');
    localStorage.removeItem('showDeleteSuccessNotification');
  }
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
.admin-icon {
  cursor: pointer;
  position: absolute;
  top: 10px;
  right: 10px;
  width: 25px;
  height: 25px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.modal-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.comment-image {
  width: 60px;
  height: 60px;
  object-fit: contain; 
  border-radius: 10px; 
  cursor: pointer; 
}
.images-container {
  display: flex;
  align-items: center; 
  gap: 10px; 
}

.images-wrapper {
  display: flex;
  gap: 10px; 
  flex-wrap: wrap;
}
</style>