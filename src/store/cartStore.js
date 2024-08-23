import { defineStore } from "pinia";
import axios from "axios"; // Для отправки запросов

export const useCartStore = defineStore("cart", () => {
  const fetchCart = async (userId, token) => {
    try {
      const response = await axios.get(`http://localhost:8080/basket/${userId}`, {
        headers: {
          Authorization: token, // Передаем токен в заголовке
        },
      });
      return response.data; // Возвращаем данные корзины
    } catch (error) {
      console.error("Ошибка получения корзины:", error.response?.data || error.message);
      throw error;
    }
  };

  return {
    fetchCart, // Экспортируем метод для получения корзины
  };
});
