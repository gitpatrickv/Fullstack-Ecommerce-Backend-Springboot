package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.Image;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.repository.ImageRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
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
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void uploadProductPhoto(String id, MultipartFile file) {
        Optional<Product> product = productRepository.findById(id);
        Image image = new Image();
        image.setProduct(product.get());
        image.setPhotoUrl(processProductImage(id, file));
        imageRepository.save(image);
    }

    @Override
    public void uploadUserPhoto(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email).get();
        user.setPhotoUrl(processUserImage(email,file));
        userRepository.save(user);
    }


    @Override
    public byte[] getPhoto(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(StringUtil.PHOTO_DIRECTORY + filename));
    }

    private String processProductImage(String id, MultipartFile image) {
        String filename = id+image.getOriginalFilename();
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
