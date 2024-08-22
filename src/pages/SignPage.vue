<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <h2>Вход в личный аккаунт</h2>
      <p>нет аккаунта? создайте!</p>
      <router-link to="/registration">
        <n-button type="warning">Создать аккаунт</n-button>
      </router-link>
      <form @submit.prevent="login">
        <input type="email" v-model="loginEmail" placeholder="Email" required autocomplete="email" />
        <input type="password" v-model="loginPassword" placeholder="Пароль" required  autocomplete="current-password" />
        <div class="buttons">
          <button type="submit">Войти</button>
        </div>
      </form>
    </div>
    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { NButton } from 'naive-ui';
 import api from '../services/api';
import { ref } from 'vue';
 import { useRouter } from 'vue-router';

 const route = useRouter();
const loginEmail = ref('');
const loginPassword = ref('');
const message = ref('');



const login = async () => {
  console.log('Login function triggered'); 
  try {
    const response = await api.post('/user/login', null, {
      params: {
        email: loginEmail.value,
        password: loginPassword.value,
      },
    });
    console.log("Полученный токен:", response.data.token);
    localStorage.setItem('token', response.data.token);
    route.push('/');
  } catch (error) {
    console.error('Ошибка:', error); 
    message.value = 'Ошибка входа: ' + (error.response?.data || 'Неизвестная ошибка');
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  width: 500px;
  box-sizing: border-box;
  gap: 10px;
}

.modal-content h2 {
  margin-top: 0;
}

.modal-content input[type="text"],
.modal-content input[type="email"],
.modal-content input[type="password"] {
  width: calc(100% - 20px);
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-content button {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  cursor: pointer;
}

</style>