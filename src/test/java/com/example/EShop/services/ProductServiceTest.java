package com.example.EShop.services;

import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.BasketRepository;
import com.example.EShop.repositories.UserRepository;
import com.example.EShop.repositories.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListProducts_WithTitle() {
        String title = "Product Title";
        List<Product> expectedProducts = new ArrayList<>();
        when(productRepository.findByTitle(title)).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.listProducts(title);

        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findByTitle(title);
    }

    @Test
    public void testListProducts_WithoutTitle() {
        List<Product> expectedProducts = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.listProducts(null);

        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testSaveProduct_WithImage() throws IOException {
        Product product = new Product();
        MockMultipartFile file = new MockMultipartFile("file", "filename.png", "image/png", "image content".getBytes());

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.saveProduct(product, file);

        verify(productRepository, times(2)).save(any(Product.class));
        assertNotNull(product.getImages());
        assertEquals(1, product.getImages().size());
        assertEquals(file.getOriginalFilename(), product.getImages().get(0).getOriginalFileName());
    }

    @Test
    public void testSaveProduct_WithoutImage() throws IOException {
        Product product = new Product();
        MockMultipartFile file = new MockMultipartFile("file", "", "image/png", new byte[0]);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.saveProduct(product, file);

        verify(productRepository, times(2)).save(any(Product.class));
        assertTrue(product.getImages().isEmpty());
    }

    @Test
    public void testGetUserByPrincipal() {
        String email = "test@example.com";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);
        User expectedUser = new User();
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User actualUser = productService.getUserByPrincipal(principal);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testGetUserByPrincipal_NullPrincipal() {
        User actualUser = productService.getUserByPrincipal(null);

        assertNotNull(actualUser);
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        productService.deleteProduct(productId);

        verify(basketRepository, times(1)).deleteProductFromBasketProducts(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }
}