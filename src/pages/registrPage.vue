<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button @click="closeModal" class="close-button">✖</button>
      <h2>Регистрация нового пользователя</h2>
      <p>Если аккаунт уже существует, то войдите</p>
      <router-link to="/signin">
        <n-button type="warning">Войти в аккаунт</n-button>
      </router-link>
      <form @submit.prevent="register">
        <input v-model="username" type="text" placeholder="Имя" required  />
        <input v-model="surname" type="text" placeholder="Фамилия" required />
        <input v-model="email" type="email" placeholder="Email" required />
        <input v-model="password" type="password" placeholder="Пароль" required />
        <div class="buttons">
          <button type="submit">Зарегистрироваться</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from '../services/api';
import { NButton } from 'naive-ui';
import router from '@/router';

const username = ref('');
const surname = ref('');
const email = ref('');
const password = ref('');

const register = async () => {
  try {
    const response = await api.post('/user/registration', null, {
      params: {
        username: username.value,
        surname: surname.value,
        email: email.value,
        password: password.value,
      },
    });
    console.log('Пользователь зарегистрирован:', response.data);
  } catch (error) {
   console.log(error)
  }
};
const closeModal = () => {
  router.push('/')
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
  position: relative;
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
.buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
input {
  width: 100%;
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
.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #333;
  position: absolute;
  top: 10px;
  left: 220px;
}
</style>
