<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button @click="closeModal" class="close-button">✖</button>
      <h2>Вход в личный аккаунт</h2>
      <p>Нет аккаунта? создайте!</p>
      <router-link to="/registration">
        <n-button type="warning">Создать аккаунт</n-button>
      </router-link>
      <form @submit.prevent="login">
        <input type="email" v-model="loginEmail" placeholder="Email" required autocomplete="email" />
        <input type="password" v-model="loginPassword" placeholder="Пароль" required  autocomplete="current-password" />
        <div class="buttons">
          <button class="registr" type="submit">Войти</button>
        </div>
      </form>
    </div>
    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/store/userStore';
import { NButton } from 'naive-ui';

const userStore = useUserStore();

const loginEmail = ref('');
const loginPassword = ref('');
const message = ref('');



const login = async () => {
  try {
    await userStore.login(loginEmail.value, loginPassword.value);
    window.history.back();
  } catch (error) {
    message.value = error.message;
  }
};

const closeModal = () => {
  window.history.back(); 
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
  gap: 10px;
}

.modal-content h2 {
  margin-top: 0;
}
form{
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
}
input {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.registr {
  width: 100%;
  display: flex;
  justify-content: center;
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
  right: 20px;
}
</style>