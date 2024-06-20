package com.example.EShop.controllers;

import com.example.EShop.controllers.BasketController;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.services.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BasketControllerTest {

    @Mock
    private BasketService basketService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Model model;

    @InjectMocks
    private BasketController basketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBasket_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        String result = basketController.getBasket(userId, model);

        assertEquals("errorPage", result);
        verify(model).addAttribute("message", "User not found");
    }

    @Test
    void testGetBasket_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);
        when(basketService.getUserProducts(user)).thenReturn(products);

        String result = basketController.getBasket(userId, model);

        assertEquals("basket", result);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testAddToBasket() {

        Long userId = 1L;
        Long productId = 2L;
        User user = new User();
        user.setId(userId);
        Product product = new Product();
        product.setId(productId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));


        String viewName = basketController.addToBasket(productId, userId, model);


        assertEquals("redirect:/", viewName);
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(basketService, times(1)).addProduct(user, product);
        verify(model, times(1)).addAttribute(eq("message"), anyString());
    }


    @Test
    void testDeleteFromBasket_ProductNotFound() {
        Long userId = 1L;
        Long productId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        String result = basketController.deleteFromBasket(userId, productId, model);

        assertEquals("redirect:/basket/" + userId, result);
        verify(model).addAttribute("message", "Error removing product from basket: Product not found");
        verifyNoInteractions(basketService);
    }

}