package com.example.EShop.services;

import com.example.EShop.dtos.ProductDto;
import com.example.EShop.models.Image;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.BasketRepository;
import com.example.EShop.repositories.ImageRepository;
import com.example.EShop.repositories.ProductRepository;
import com.example.EShop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final ImageRepository imageRepository;


    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        List<Product> products = productRepository.findAll();
        return products;
    }

    public List<ProductDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductDto convertToDto(Product product) {
        Image image = imageRepository.findById(product.getPreviewImageId()).orElse(null);
        String base64Image = image != null ? Base64.getEncoder().encodeToString(image.getBytes()) : null;


        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getCount(),
                product.getPrice(),
                product.getPreviewImageId(),
                product.getDiscountPrice(),
                product.getCategory(),
                product.getComments(),
                base64Image
        );
    }

    @Transactional
    public void saveProduct(Product product, MultipartFile file1) throws IOException {
        Image image1;

        if (file1.getSize() != 0 && file1.getSize() > 0) {
            image1 = toImageEntity(file1);

            product.addImageToProduct(image1);
        }

        log.info("Saving new Product. Title: {}", product.getTitle());
        Product productFromDB = productRepository.save(product);
        if (!productFromDB.getImages().isEmpty()) {
            productFromDB.setPreviewImageId(productFromDB.getImages().get(0).getId());
        }
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        basketRepository.deleteProductFromBasketProducts(id);
        productRepository.deleteById(id);

    }


//    public Product findById(Long productId) {
//        return productRepository.findById(productId);
//    }
}
