package com.example.EShop.controllers;


import com.example.EShop.models.Image;
import com.example.EShop.repositories.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;
import java.io.IOException;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageController imageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void testGetImageById_ImageFound() throws Exception {
        Long imageId = 1L;
        Image image = createTestImage(imageId);
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        mockMvc.perform(get("/images/{id}", imageId))
                .andExpect(status().isOk())
                .andExpect(header().string("fileName", image.getOriginalFileName()))
                .andExpect(content().contentType(MediaType.valueOf(image.getContentType())))
                .andExpect(content().bytes(image.getBytes()));

        verify(imageRepository, times(1)).findById(imageId);
        verifyNoMoreInteractions(imageRepository);
    }

    @Test
    void testGetImageById_ImageNotFound() throws Exception {
        Long imageId = 1L;
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/images/{id}", imageId))
                .andExpect(status().isNotFound());

        verify(imageRepository, times(1)).findById(imageId);
        verifyNoMoreInteractions(imageRepository);
    }

    private Image createTestImage(Long id) throws IOException {
        Image image = new Image();
        image.setId(id);
        image.setOriginalFileName("test.jpg");
        image.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
        image.setSize(1024L);
        image.setBytes(new byte[]{0x12, 0x34, 0x56, 0x78}); // Example byte array
        return image;
    }
}