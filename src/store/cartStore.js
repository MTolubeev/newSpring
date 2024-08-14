import { defineStore } from "pinia";
import { ref, watch } from "vue";

export const useCartStore = defineStore("cart", () => {
  const cartItems = ref([]);

  const loadCartFromLocalStorage = () => {
    const storedItems = JSON.parse(localStorage.getItem("cartItems")) || [];
    cartItems.value = storedItems;
  };

  const saveCartToLocalStorage = () => {
    localStorage.setItem("cartItems", JSON.stringify(cartItems.value));
  };

  
  watch(cartItems, () => {
    saveCartToLocalStorage();
  });

  const addToCart = (item) => {
    if (!cartItems.value.find((cartItem) => cartItem.id === item.id)) {
      cartItems.value.push(item);
    }
  };

  const removeFromCart = (itemId) => {
    cartItems.value = cartItems.value.filter((item) => item.id !== itemId);
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
