package com.example.EShop.services;



import com.example.EShop.models.Basket;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.BasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketService basketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct_NewBasket() {
        User user = new User();
        Product product = new Product();
        product.setCount(10);

        when(basketRepository.findByUser(user)).thenReturn(null);

        basketService.addProduct(user, product);

        ArgumentCaptor<Basket> basketCaptor = ArgumentCaptor.forClass(Basket.class);
        verify(basketRepository).save(basketCaptor.capture());

        Basket savedBasket = basketCaptor.getValue();
        assertEquals(1, savedBasket.getProducts().size());
        assertEquals(product, savedBasket.getProducts().get(0));
        assertEquals(9, product.getCount()); // проверка уменьшения количества товара
    }

    @Test
    public void testAddProduct_ExistingBasket() {
        User user = new User();
        Product product = new Product();
        product.setCount(10);

        Basket basket = new Basket();
        basket.setUser(user);
        basket.setProducts(new ArrayList<>());

        when(basketRepository.findByUser(user)).thenReturn(basket);

        basketService.addProduct(user, product);

        assertEquals(1, basket.getProducts().size());
        assertEquals(product, basket.getProducts().get(0));
        assertEquals(9, product.getCount()); // проверка уменьшения количества товара
        verify(basketRepository).save(basket);
    }

    @Test
    public void testGetUserProducts_NoProducts() {
        User user = new User();

        when(basketRepository.findByUser(user)).thenReturn(null);

        List<Product> products = basketService.getUserProducts(user);
        assertTrue(products.isEmpty());
    }

    @Test
    public void testGetUserProducts_WithProducts() {
        User user = new User();
        Product product = new Product();

        Basket basket = new Basket();
        basket.setUser(user);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        basket.setProducts(productList);

        when(basketRepository.findByUser(user)).thenReturn(basket);

        List<Product> products = basketService.getUserProducts(user);
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testDeleteProduct() {
        User user = new User();
        Product product = new Product();
        product.setCount(10);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Basket basket = new Basket();
        basket.setUser(user);
        basket.setProducts(products);

        when(basketRepository.findByUser(user)).thenReturn(basket);

        basketService.deleteProduct(user, product);

        assertTrue(basket.getProducts().isEmpty());
        assertEquals(11, product.getCount()); // проверка увеличения количества товара
        verify(basketRepository).save(basket);
    }

    @Test
    public void testReturnBasketSize_EmptyBasket() {
        User user = new User();

        when(basketRepository.findByUser(user)).thenReturn(null);

        int size = basketService.returnBasketSize(user);
        assertEquals(0, size);
    }

    @Test
    public void testReturnBasketSize_WithProducts() {
        User user = new User();
        Product product = new Product();
        List<Product> products = new ArrayList<>();
        products.add(product);

        Basket basket = new Basket();
        basket.setUser(user);
        basket.setProducts(products);

        when(basketRepository.findByUser(user)).thenReturn(basket);

        int size = basketService.returnBasketSize(user);
        assertEquals(1, size);
    }
}