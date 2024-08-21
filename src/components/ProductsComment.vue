<template>
  <h2>Отзывы о товаре</h2>
    <div class="comments__wrapper">
      <div v-if="comments.length > 0" class="comments__items"> 
        <n-card v-for="comment in comments" :key="comment.id" >
          <div class="profile">
            <img src="@/assets/Union.svg" alt="user">
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
        </n-card>
      </div>
      <div v-else>
        <h3>Комментариев пока что нет</h3>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps } from 'vue';
  import { NCard } from 'naive-ui';
  

defineProps({
    comments: {
      type: Array,
      required: true
    }
  });

  const maxStars = 5;
  </script>
  
<style scoped>
.comments__wrapper{
  display: flex;
  align-items: center;
  justify-content: center;
}
.comments__items{
  display: flex;
  gap: 30px;
  margin-top: 30px;
}
.profile{
  display: flex;
  gap: 10px;
}
.stars {
  font-size: 1.5em;
  color: gold; 
}
h2{
  text-align: center;
  margin-top: 10px;
}
 .n-card{
  width: 300px;
  border-radius: 20px;
 }
  </style>
  