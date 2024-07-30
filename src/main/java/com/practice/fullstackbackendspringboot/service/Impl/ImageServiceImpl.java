package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.ImageService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void uploadProductPhoto(String productId, MultipartFile[] files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + productId));

        for(MultipartFile file : files){
            Image image = new Image();
            image.setProduct(product);
            image.setPhotoUrl(processProductImage(productId, file));
            imageRepository.save(image);
        }
    }

    @Override
    public void uploadUserPhoto(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email).get();
        user.setPhotoUrl(processUserImage(email,file));
        userRepository.save(user);
    }

    @Override
    public void uploadStorePhoto(String email, String storeId, MultipartFile file) {
        userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if(optionalStore.isPresent()) {
            Store store = optionalStore.get();
            store.setPhotoUrl(processUserImage(storeId, file));
            storeRepository.save(store);
        }
    }

    @Override
    public void uploadCategoryPhoto(String email, String categoryId, MultipartFile file) {
        userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setCategoryPhotoUrl(processUserImage(categoryId,file));
            categoryRepository.save(category);
        }
    }

    @Override
    public byte[] getPhoto(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(StringUtil.PHOTO_DIRECTORY + filename));
    }

    private String processProductImage(String productId, MultipartFile image) {
        String filename = productId + "_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
        try {
            Path fileStorageLocation = Paths.get(StringUtil.PHOTO_DIRECTORY).toAbsolutePath().normalize();

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename));

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/product/image/" + filename).toUriString();


        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    }

    private String processUserImage(String id, MultipartFile image) {
        String filename = id+image.getOriginalFilename();
        try {
            Path fileStorageLocation = Paths.get(StringUtil.PHOTO_DIRECTORY).toAbsolutePath().normalize();

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/user/image/" + filename).toUriString();


        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    }



}
