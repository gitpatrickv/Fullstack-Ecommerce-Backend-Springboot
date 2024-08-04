package com.practice.fullstackbackendspringboot.config;


import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ImageRepository imageRepository;

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
            if(userRepository.count() == 0 && categoryRepository.count() == 0 && storeRepository.count() == 0 && productRepository.count() == 0) {
                User admin = new User();
                admin.setName("ADMIN");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);

                User customer = new User();
                customer.setName("customer customer");
                customer.setEmail("customer@gmail.com");
                customer.setAddress("123 customer address");
                customer.setContactNumber("099999999");
                customer.setPassword(passwordEncoder.encode("12345678"));
                customer.setRole(Role.USER);
                userRepository.save(customer);

                User seller = new User();
                seller.setName("seller seller");
                seller.setEmail("seller@gmail.com");
                seller.setAddress("123 seller address");
                seller.setContactNumber("0912345678");
                seller.setPassword(passwordEncoder.encode("12345678"));
                seller.setRole(Role.SELLER);
                userRepository.save(seller);

                Store store = new Store();
                store.setStoreName("My Store");
                store.setStoreDescription("My Store Description");
                store.setAddress("Seller Address");
                store.setContactNumber("0912345678");
                store.setShippingFee(99.0);
                store.setProductCount(2L);
                store.setOnline(true);
                store.setUser(seller);
                storeRepository.save(store);

                Category category1 = new Category();
                category1.setCategoryName("Men's Apparel");
                category1.setCategoryPhotoUrl("https://img.freepik.com/premium-vector/men-s-clothing-store-logo-clothing-store-transparent-background-clothing-shop-logo-vector_148524-756.jpg");
                categoryRepository.save(category1);

                Category category2 = new Category();
                category2.setCategoryName("Mobiles & Gadgets");
                category2.setCategoryPhotoUrl("https://profit.pakistantoday.com.pk/wp-content/uploads/2023/07/pp.jpg");
                categoryRepository.save(category2);

                Category category3 = new Category();
                category3.setCategoryName("Mobiles Accessories");
                category3.setCategoryPhotoUrl("https://storeassets.im-cdn.com/products/aa0820/8gZ3otP9Rm6MErz8CqpM_Best-mobile-accessories-brands-on-Amazon_0x0_webp.jpg");
                categoryRepository.save(category3);

                Category category4 = new Category();
                category4.setCategoryName("Home Appliances");
                category4.setCategoryPhotoUrl("https://lessandra.com.ph/assets/BlogImages/efficient-home-appliances/top-efficient-appliances-for-your-affordable-house-and-lot-in-lessandra-homes.png");
                categoryRepository.save(category4);

                Category category5 = new Category();
                category5.setCategoryName("Babies & Kids");
                category5.setCategoryPhotoUrl("https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/health/wp-content/uploads/2021/05/how_long_does_baby_formula_last-getty_creative.jpg");
                categoryRepository.save(category5);

                Category category6 = new Category();
                category6.setCategoryName("Home & Living");
                category6.setCategoryPhotoUrl("https://d7fcfvvxwoz9e.cloudfront.net/dom43635/wp-content/uploads/2018/12/drill-tools.png");
                categoryRepository.save(category6);

                Category category7 = new Category();
                category7.setCategoryName("Groceries");
                category7.setCategoryPhotoUrl("https://www.cnet.com/a/img/resize/00e47bf776182602208cf7f3b49a52a13866fa15/hub/2022/06/02/89e071d6-f397-45f1-aa2c-df0bd91666e9/hr.jpg?auto=webp&width=1200");
                categoryRepository.save(category7);

                Category category8 = new Category();
                category8.setCategoryName("Toys & Games");
                category8.setCategoryPhotoUrl("https://cdn.shopify.com/s/files/1/0326/2971/9176/collections/toys-games-444426_600x600_crop_center.jpg?v=1645577311");
                categoryRepository.save(category8);

                Category category9 = new Category();
                category9.setCategoryName("Women's Bags");
                category9.setCategoryPhotoUrl("https://i.ebayimg.com/00/s/ODAwWDgwMA==/z/1egAAOSw3ohhubJp/$_57.JPG?set_id=8800005007");
                categoryRepository.save(category9);

                Category category10 = new Category();
                category10.setCategoryName("Women's Accessories");
                category10.setCategoryPhotoUrl("https://jlood.com/cdn/shop/articles/how-to-choose-women-accessories-in-egypt.jpg?v=1707887955&width=600");
                categoryRepository.save(category10);

                Category category11 = new Category();
                category11.setCategoryName("Women's Shoes");
                category11.setCategoryPhotoUrl("https://newfemme.co/img_resize/artikel/264/1520/sejarah-high-heels");
                categoryRepository.save(category11);

                Category category12 = new Category();
                category12.setCategoryName("Pet Care");
                category12.setCategoryPhotoUrl("https://www.sgs.com/-/media/sgscorp/images/health-and-nutrition/dog-food-in-bowl-and-dog-biscuits.cdn.en-PH.1.jpg");
                categoryRepository.save(category12);

                Category category13 = new Category();
                category13.setCategoryName("Audio");
                category13.setCategoryPhotoUrl("https://netdna.coolthings.com/wp-content/uploads/2013/03/nocs1.jpg");
                categoryRepository.save(category13);

                Category category14 = new Category();
                category14.setCategoryName("Women's Apparel");
                category14.setCategoryPhotoUrl("https://i.pinimg.com/736x/32/9b/38/329b387a18719a6e7d44b540ce3845f6.jpg");
                categoryRepository.save(category14);

                Category category15 = new Category();
                category15.setCategoryName("Personal Care");
                category15.setCategoryPhotoUrl("https://img.freepik.com/premium-photo/cosmetic-skincare-product-blank-plastic-package-white-unbranded-lotion-balsam-hand-creme-toothpaste-mockup_285651-339.jpg");
                categoryRepository.save(category15);

                Category category16 = new Category();
                category16.setCategoryName("Fragrances");
                category16.setCategoryPhotoUrl("https://media.gq-magazine.co.uk/photos/641870abb85d62fb47db65e8/16:9/w_2580,c_limit/Vetiver%20fragrances_FragHP.jpg");
                categoryRepository.save(category16);

                Category category17 = new Category();
                category17.setCategoryName("Laptops & Computers");
                category17.setCategoryPhotoUrl("https://media.gcflearnfree.org/content/55e073167dd48174331f5169_01_17_2014/setup_Step-1_on-desk_EDIT-02.jpg");
                categoryRepository.save(category17);

                Category category18 = new Category();
                category18.setCategoryName("Cameras");
                category18.setCategoryPhotoUrl("https://cdn.mos.cms.futurecdn.net/Ekc54rx2YMgRt5ycD5KYf5.jpg");
                categoryRepository.save(category18);

                Category category19 = new Category();
                category19.setCategoryName("Sports");
                category19.setCategoryPhotoUrl("https://i.etsystatic.com/20568465/r/il/761a9c/1979162807/il_fullxfull.1979162807_bzf1.jpg");
                categoryRepository.save(category19);

                Category category20 = new Category();
                category20.setCategoryName("Men's Accessories");
                category20.setCategoryPhotoUrl("https://nextluxury.com/wp-content/uploads/Top-15-Fashion-Accessories-For-Men-1.jpg");
                categoryRepository.save(category20);

                Category category21 = new Category();
                category21.setCategoryName("Men's Shoes");
                category21.setCategoryPhotoUrl("https://reebok.bynder.com/m/32e5f94b44c3ed31/webimage-23SS_Men-Shoes_FD_IWP-Tile-BB_MB.png");
                categoryRepository.save(category21);

                Category category22 = new Category();
                category22.setCategoryName("Motors");
                category22.setCategoryPhotoUrl("https://img.freepik.com/premium-photo/sketch-motorcycle-helmet-black-background-vector-illustration_888135-412.jpg");
                categoryRepository.save(category22);

                Category category23 = new Category();
                category23.setCategoryName("Stationery");
                category23.setCategoryPhotoUrl("https://stationers.pk/cdn/shop/articles/7_Must_Have_Student_Stationery_Supplies_In_High_School.jpg?v=1635331870");
                categoryRepository.save(category23);

                Category category24 = new Category();
                category24.setCategoryName("Gaming");
                category24.setCategoryPhotoUrl("https://hips.hearstapps.com/hmg-prod/images/gh-index-gamingconsoles-052-print-preview-1659705142.jpg");
                categoryRepository.save(category24);

                Product noVarProduct = new Product();
                noVarProduct.setProductName("No Variation");
                noVarProduct.setProductDescription("No Variation Product Description");
                noVarProduct.setListed(true);
                noVarProduct.setCategory(category1);
                noVarProduct.setStore(store);
                noVarProduct.setUser(seller);
                productRepository.save(noVarProduct);

                Inventory inventory = new Inventory();
                inventory.setPrice(100.0);
                inventory.setQuantity(10L);
                inventory.setProduct(noVarProduct);
                inventoryRepository.save(inventory);

                Image image = new Image();
                image.setPhotoUrl("https://t4.ftcdn.net/jpg/00/38/13/73/360_F_38137330_gUbR3ZXBc5J5g4pRkaC8TYZQA62OZhx5.jpg");
                image.setProduct(noVarProduct);
                imageRepository.save(image);

                Product product1 = new Product();
                product1.setProductName("With Variation");
                product1.setProductDescription("With Variation Product Description");
                product1.setListed(true);
                product1.setCategory(category2);
                product1.setStore(store);
                product1.setUser(seller);
                productRepository.save(product1);

                Inventory inventory1 = new Inventory();
                inventory1.setPrice(100.0);
                inventory1.setQuantity(10L);
                inventory1.setColors("Red");
                inventory1.setSizes("XS");
                inventory1.setProduct(product1);
                inventoryRepository.save(inventory1);

                Inventory inventory2 = new Inventory();
                inventory2.setPrice(110.0);
                inventory2.setQuantity(15L);
                inventory2.setColors("Red");
                inventory2.setSizes("S");
                inventory2.setProduct(product1);
                inventoryRepository.save(inventory2);

                Inventory inventory3 = new Inventory();
                inventory3.setPrice(100.0);
                inventory3.setQuantity(20L);
                inventory3.setColors("Blue");
                inventory3.setSizes("XS");
                inventory3.setProduct(product1);
                inventoryRepository.save(inventory3);

                Inventory inventory4 = new Inventory();
                inventory4.setPrice(110.0);
                inventory4.setQuantity(5L);
                inventory4.setColors("Blue");
                inventory4.setSizes("S");
                inventory4.setProduct(product1);
                inventoryRepository.save(inventory4);

                Inventory inventory5 = new Inventory();
                inventory5.setPrice(120.0);
                inventory5.setQuantity(0L);
                inventory5.setColors("Blue");
                inventory5.setSizes("M");
                inventory5.setProduct(product1);
                inventoryRepository.save(inventory5);

                Inventory inventory6 = new Inventory();
                inventory6.setPrice(120.0);
                inventory6.setQuantity(99L);
                inventory6.setColors("Red");
                inventory6.setSizes("M");
                inventory6.setProduct(product1);
                inventoryRepository.save(inventory6);

                Image image1 = new Image();
                image1.setPhotoUrl("https://t4.ftcdn.net/jpg/00/38/13/73/360_F_38137330_gUbR3ZXBc5J5g4pRkaC8TYZQA62OZhx5.jpg");
                image1.setProduct(product1);
                imageRepository.save(image1);
            }
        };
    }
}

