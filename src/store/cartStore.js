import { defineStore } from 'pinia';
import axios from 'axios';
import { ref } from 'vue';

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref([]); 

  const fetchCart = async (userId, token) => {
    try {
      const response = await axios.get(`http://localhost:8080/basket/${userId}`, {
        headers: {
          Authorization: token,
        },
      });
  
      cartItems.value = response.data.map((item) => {
        return {
          ...item,
          imageUrl: item.base64Image ? `data:image/png;base64,${item.base64Image}` : null,
        };
      });
  
      return cartItems.value;
    } catch (error) {
      console.error("Ошибка получения корзины:", error.response?.data || error.message);
      throw error;
    }
  };

  const addToCart = async (productId, token) => {
    try {
      const response = await axios.post(`http://localhost:8080/basket/addToBasket`, null, {
        params: {
          productId
        },
        headers: {
          Authorization: token
        }
      });

      await fetchCart(response.data.userId, token);  
      return response.data;
    } catch (error) {
      console.log(error);
    }
  };

  const removeFromcart = async (productId, token) => {
    try {
      const response = await axios.post(`http://localhost:8080/basket/delete/${productId}`, null, {
        params: {
          productId
        },
        headers: {
          Authorization: token
        }
      });

      await fetchCart(response.data.userId, token);  
      return response.data;
    } catch (error) {
      console.log(error);
    }
  };

  return {
    cartItems,
    fetchCart,
    addToCart,
    removeFromcart
  };
});
