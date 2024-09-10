package com.example.EShop.services;

import com.example.EShop.controllers.CommentController;
import com.example.EShop.dtos.CategoryDto;
import com.example.EShop.dtos.CommentDto;
import com.example.EShop.dtos.ProductDto;
import com.example.EShop.models.Comment;
import com.example.EShop.models.Image;
import com.example.EShop.models.Product;
import com.example.EShop.models.User;
import com.example.EShop.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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


    public ProductDto findAllProducts(Long id) {
        return productRepository.findById(id)
                .map(this::convertProductToDto)
                .orElse(null);  // Вернуть null, если продукт не найден
    }
    public List<ProductDto> findAllProducts() {

        return productRepository.findAll().stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList());
    }


    private ProductDto convertProductToDto(Product product) {
        Image image = imageRepository.findById(product.getPreviewImageId()).orElse(null);
        String base64Image = image != null ? Base64.getEncoder().encodeToString(image.getBytes()) : null;
        List<CategoryDto> categories = new ArrayList<>();

        List<Comment> commentsRaw = product.getComments();
        List<CommentDto> comments = commentsRaw.stream()
                .map(this::convertCommentToDto)
                .collect(Collectors.toList());

        String[] categoryStrings = product.getCategory().split(",");
        String[] categoryOrders = product.getCategoryOrder().split(",");

        for (int i = 0; i < categoryStrings.length; i++) {
            String[] categoryParts = categoryStrings[i].split("/");
            String name = categoryParts.length > 0 ? categoryParts[0] : null;
            String subcategory = categoryParts.length > 1 ? categoryParts[1] : null;
            String subsubcategory = categoryParts.length > 2 ? categoryParts[2] : null;

            // Получение порядков для каждой категории
            Integer order = categoryOrders.length > 0 && i < categoryOrders.length ? Integer.parseInt(categoryOrders[i]) : null;
            Integer subcategoryOrder = null;
            Integer subsubcategoryOrder = null;

            // Если нужно добавить порядки подкатегорий
            if (categoryParts.length > 1) {
                subcategoryOrder = (categoryOrders.length > 1 && i < categoryOrders.length) ? Integer.parseInt(categoryOrders[i + 1]) : null;
            }
            if (categoryParts.length > 2) {
                subsubcategoryOrder = (categoryOrders.length > 2 && i < categoryOrders.length) ? Integer.parseInt(categoryOrders[i + 2]) : null;
            }

            categories.add(new CategoryDto(name, subcategory, subsubcategory, order, subcategoryOrder, subsubcategoryOrder));
        }
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getCount(),
                product.getPrice(),
                product.getPreviewImageId(),
                product.getDiscountPrice(),
                categories,
                comments,
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


    private CommentDto convertCommentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setImages(comment.getImages());
        dto.setScore(comment.getScore());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setProductId(comment.getProduct().getId());
        dto.setProductTitle(comment.getProduct().getTitle());
        return dto;
    }
    public void reorderCategories(Long productId, List<CategoryDto> newOrder) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        StringBuilder newCategoryOrder = new StringBuilder();
        StringBuilder newCategoryString = new StringBuilder();

        for (CategoryDto category : newOrder) {
            newCategoryString.append(category.getName()).append("/")
                    .append(category.getSubcategory()).append("/")
                    .append(category.getSubsubcategory()).append(",");
            newCategoryOrder.append(category.getOrder()).append(",");
        }

        product.setCategory(newCategoryString.toString());
        product.setCategoryOrder(newCategoryOrder.toString());

        productRepository.save(product);
    }
}
