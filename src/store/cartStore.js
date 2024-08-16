import { defineStore } from "pinia";
import { ref } from "vue";

export const useCartStore = defineStore("cart", () => {
  const cartItems = ref([]);

  const loadCartFromLocalStorage = () => {
    const storedItems = JSON.parse(sessionStorage.getItem("cartItems")) || [];
    cartItems.value = storedItems;
  };

  const saveCartToLocalStorage = () => {
    sessionStorage.setItem("cartItems", JSON.stringify(cartItems.value));
  };

  const addToCart = (item) => {
    if (!cartItems.value.find((cartItem) => cartItem.id === item.id)) {
      cartItems.value.push(item);
      saveCartToLocalStorage(); 
    }
  };

  const removeFromCart = (itemId) => {
    cartItems.value = cartItems.value.filter((item) => item.id !== itemId);
    saveCartToLocalStorage(); 
  };

  const isInCart = (itemId) =>
    cartItems.value.some((item) => item.id === itemId);

  loadCartFromLocalStorage();

  return {
    cartItems,
    addToCart,
    removeFromCart,
    isInCart,
  };
});
