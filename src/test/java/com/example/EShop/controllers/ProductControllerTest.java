package com.example.EShop.controllers;

import com.example.EShop.models.Product;
import com.example.EShop.services.BasketService;
import com.example.EShop.services.ProductService;
import com.example.EShop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private BasketService basketService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }


    @Test
    void testCreateProduct() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file1", "test.jpg", "image/jpeg", "Test image".getBytes());
        Product product = new Product();
        product.setTitle("New Product");

        mockMvc.perform(multipart("/product/create")
                        .file(file1)
                        .param("title", product.getTitle()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(productService, times(1)).saveProduct(any(), eq(file1));
        verifyNoMoreInteractions(productService, basketService, userService);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(post("/product/delete/{id}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(productService, times(1)).deleteProduct(productId);
        verifyNoMoreInteractions(productService, basketService, userService);
    }
}