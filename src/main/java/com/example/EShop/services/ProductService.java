package com.example.EShop.services;

import com.example.EShop.dtos.CategoryDto;
import com.example.EShop.dtos.CommentDto;
import com.example.EShop.dtos.ProductDto;
import com.example.EShop.models.Comment;
import com.example.EShop.models.Image;
import com.example.EShop.models.Product;
import com.example.EShop.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BasketRepository basketRepository;
    private final ImageRepository imageRepository;


    public ProductDto findAllProducts(Long id) {
        return productRepository.findById(id)
                .map(this::convertProductToDto)
                .orElse(null);  // Вернуть null, если продукт не найден
    }

    public List<ProductDto> findAllProducts() {

        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList());
    }


    private ProductDto convertProductToDto(Product product) {
        Image image = imageRepository.findById(product.getPreviewImageId()).orElse(null);
        String base64Image = image != null ? Base64.getEncoder().encodeToString(image.getBytes()) : null;

        List<Comment> commentsRaw = product.getComments();
        List<CommentDto> comments = commentsRaw.stream()
                .map(this::convertCommentToDto)
                .collect(Collectors.toList());

        List<CategoryDto> categories = new ArrayList<>();
        String[] categoryStrings = product.getCategory().split(",");
        String[] categoryOrders = product.getCategoryOrder().split("/");

        for (int i = 0; i < categoryStrings.length; i++) {
            String[] categoryParts = categoryStrings[i].split("/");
            String name = categoryParts.length > 0 ? categoryParts[0] : null;
            String subcategory = categoryParts.length > 1 ? categoryParts[1] : null;
            String subsubcategory = categoryParts.length > 2 ? categoryParts[2] : null;

            String[] orderStrings = categoryOrders[i].split(",");
            Integer[] orderInt = new Integer[orderStrings.length];
            for (int k = 0; k < orderStrings.length; k++) {
                orderInt[k] = Integer.parseInt(orderStrings[k]);
            }

            Integer order =  orderInt[0];
            Integer subcategoryOrder = null;
            Integer subsubcategoryOrder = null;

            if (categoryParts.length > 1) {
                subcategoryOrder = (orderInt.length > 1 && i < orderInt.length) ? orderInt[1] : null;
            }
            if (categoryParts.length > 2) {
                subsubcategoryOrder = (orderInt.length > 2 && i < orderInt.length) ? orderInt[2] : null;
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


    public void fillCategoryOrder(Product product) {
        Map<String, Integer> categoryMap = getAllCategories(1);
        Map<String, Integer> subcategoryMap = getAllCategories(2);
        Map<String, Integer> subsubcategoryMap = getAllCategories(3);

        // Получаем категорию продукта
        String categoryString = product.getCategory();
        String[] categoryParts = categoryString.split(",");

        StringBuilder categoryOrderBuilder = new StringBuilder();

        for (String categoryPart : categoryParts) {
            // Разбиваем строку категории на части (категория, подкатегория, подподкатегория)
            String[] parts = categoryPart.split("/");
            Integer categoryOrder = null;
            Integer subcategoryOrder = null;
            Integer subsubcategoryOrder = null;

            // Проверяем или добавляем порядковые номера для каждой категории
            // Основная категория
            if (parts.length > 0 && !parts[0].isEmpty()) {
                categoryOrder = categoryMap.get(parts[0]);
                if (categoryOrder == null) {
                    // Назначаем новый порядковый номер
                    categoryOrder = categoryMap.size() + 1;
                    categoryMap.put(parts[0], categoryOrder);
                }
            }

            // Подкатегория
            if (parts.length > 1 && !parts[1].isEmpty()) {
                subcategoryOrder = subcategoryMap.get(parts[1]);
                if (subcategoryOrder == null) {
                    // Назначаем новый порядковый номер
                    subcategoryOrder = subcategoryMap.size() + 1;
                    subcategoryMap.put(parts[1], subcategoryOrder);
                }
            }

            // Подподкатегория
            if (parts.length > 2 && !parts[2].isEmpty()) {
                subsubcategoryOrder = subsubcategoryMap.get(parts[2]);
                if (subsubcategoryOrder == null) {
                    // Назначаем новый порядковый номер
                    subsubcategoryOrder = subsubcategoryMap.size() + 1;
                    subsubcategoryMap.put(parts[2], subsubcategoryOrder);
                }
            }

            // Формируем строку порядков
            if (categoryOrder != null) {
                categoryOrderBuilder.append(categoryOrder);
            }

            if (subcategoryOrder != null) {
                categoryOrderBuilder.append(",").append(subcategoryOrder);
            }

            if (subsubcategoryOrder != null) {
                categoryOrderBuilder.append(",").append(subsubcategoryOrder);
            }

            categoryOrderBuilder.append("/");
        }

        // Удаляем последний символ "/"
        if (categoryOrderBuilder.length() > 0) {
            categoryOrderBuilder.setLength(categoryOrderBuilder.length() - 1);
        }

        // Устанавливаем новое значение categoryOrder для продукта
        product.setCategoryOrder(categoryOrderBuilder.toString());
    }


    public Map<String, Integer> getAllCategories(Integer w) {
        Map<String, Integer> categoryMap = new HashMap<>();
        Map<String, Integer> subcategoryMap = new HashMap<>();
        Map<String, Integer> subsubcategoryMap = new HashMap<>();

        // Получаем все продукты из базы данных
        List<Product> allProducts = productRepository.findAll();

        // Проходим по каждому продукту
        for (Product product : allProducts) {
            // Разбиваем строку категорий по запятой, чтобы получить каждую категорию отдельно
            String[] categoryStrings = product.getCategory().split(",");
            // Разбиваем строку categoryOrder по "/"
            String[] categoryOrders = product.getCategoryOrder().split("/");

            // Проходим по каждой категории и её порядку
            for (int i = 0; i < categoryStrings.length; i++) {
                // Разбиваем строку категории по "/"
                String[] categoryParts = categoryStrings[i].split("/");

                // Разбиваем строку order по ","
                String[] orderStrings = categoryOrders[i].split(",");
                Integer[] orderInt = new Integer[orderStrings.length];

                // Преобразуем строковые порядки в Integer
                for (int k = 0; k < orderStrings.length; k++) {
                    orderInt[k] = Integer.parseInt(orderStrings[k]);
                }

                // Обрабатываем первую категорию
                if (categoryParts.length > 0 && !categoryParts[0].isEmpty()) {
                    categoryMap.put(categoryParts[0], orderInt[0]);
                }

                // Обрабатываем подкатегорию
                if (categoryParts.length > 1 && !categoryParts[1].isEmpty()) {
                    subcategoryMap.put(categoryParts[1], orderInt[1]);

                }

                // Обрабатываем подподкатегорию
                if (categoryParts.length > 2 && !categoryParts[2].isEmpty()) {
                    subsubcategoryMap.put(categoryParts[2], orderInt[2]);

                }

            }
        }
        if (w == 1) return categoryMap;
        if (w == 2) return subcategoryMap;
        else return subsubcategoryMap;


    }


    @Transactional
    public void saveProduct(Product product, String category)  {
//        Image image1;
//
//        if (file1.getSize() != 0 && file1.getSize() > 0) {
//            image1 = toImageEntity(file1);
//
//            product.addImageToProduct(image1);
//        }
        product.setCategory(category);

        fillCategoryOrder(product);
        log.info("Saving new Product. Title: {}", product.getTitle());
        Product productFromDB = productRepository.save(product);
        if (!productFromDB.getImages().isEmpty()) {
            productFromDB.setPreviewImageId(productFromDB.getImages().get(0).getId());
        }
        productRepository.save(product);
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
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        StringBuilder categoryBuilder = new StringBuilder();
        StringBuilder categoryOrderBuilder = new StringBuilder();

        for (CategoryDto categoryDto : newOrder) {
            String category = categoryDto.getName();
            if (categoryDto.getSubcategory() != null) {
                category += "/" + categoryDto.getSubcategory();
            }
            if (categoryDto.getSubsubcategory() != null) {
                category += "/" + categoryDto.getSubsubcategory();
            }
            if (categoryBuilder.length() > 0) {
                categoryBuilder.append(",");
            }
            categoryBuilder.append(category);

            String order = categoryDto.getOrder().toString();
            if (categoryDto.getSubcategoryOrder() != null) {
                order += "," + categoryDto.getSubcategoryOrder();
            }
            if (categoryDto.getSubsubcategoryOrder() != null) {
                order += "," + categoryDto.getSubsubcategoryOrder();
            }
            if (categoryOrderBuilder.length() > 0) {
                categoryOrderBuilder.append("/");
            }
            categoryOrderBuilder.append(order);
        }

        product.setCategory(categoryBuilder.toString());
        product.setCategoryOrder(categoryOrderBuilder.toString());

        productRepository.save(product);
    }

}

