package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.ProductImage;
import com.practice.fullstackbackendspringboot.repository.ProductImageRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.service.ProductImageService;
import com.practice.fullstackbackendspringboot.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductRepository productRepository;

    @Override
    public void uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for user ID: {}", id);

        Optional<Product> product = productRepository.findById(id);

        ProductImage productImage = new ProductImage();
        productImage.setProduct(product.get());
        productImage.setPhotoUrl(processImage(id,file));
        productImageRepository.save(productImage);
        log.info(productImage.getPhotoUrl());
    }

    private String getFileExtension(String filename){
        return Optional.of(filename)
                .filter(name -> name.contains("."))
                .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1))
                .orElse(".png");
    }

    private String processImage(String id, MultipartFile image){
        String filename = id + getFileExtension(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(StringUtils.PHOTO_DIRECTORY).toAbsolutePath().normalize();

            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/product/image/" + filename).toUriString();
        }
        catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    }

}
