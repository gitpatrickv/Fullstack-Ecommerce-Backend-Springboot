# Shopee E-Commerce Platform Clone

## Introduction
Welcome to the Shopee E-Commerce Platform Clone! This project is an imitation of the popular e-commerce platform, Shopee, designed to replicate its core features and functionalities. The goal of this project is to provide a comprehensive, full-stack implementation that covers everything from user authentication, product management, and shopping cart features, to payment processing, order management, product rating, store service rating, and the ability to follow your favorite stores.

The project utilizes React and TypeScript for the frontend, with Chakra UI for styling and Zustand for state management. The backend is powered by Java and Spring Boot, ensuring secure login through Spring Security with JWT-based authentication.

## Features

- **User Authentication & Authorization:** Secure sign-up, login, and role-based access control (admin, seller, and buyer).
- **Product Management:** Create, update, delete, and manage product listings with support for multiple categories, sizes, and colors.
- **Search & Filtering:** Advanced search capabilities with filters based on price, category, rating, and more.
- **Shopping Cart & Checkout:** Seamless shopping cart experience with options for payment via Stripe or cash on delivery.
- **Order Management:** Track order history, and view delivery status.

## Tech Stack

- **Frontend:** React, TypeScript, Chakra UI, React Query, Zustand
- **Backend:** Spring Boot, Java, MySql
- **Authentication:** Spring Security, JWT
- **Payments:** Stripe API integration

## Getting Started

1. Clone the Repository:

- **For the frontend:**
```bash
git@github.com:gitpatrickv/FullStack-Ecommerce-Frontend-React.git
```
- **For the backend:**
```bash
git@github.com:gitpatrickv/Fullstack-Ecommerce-Backend-Springboot.git
```

2. Install Frontend Dependencies in the terminal:
   ```bash
   npm install
   ```
   
3. Install Backend Dependencies in the terminal:
    ```bash
   mvn clean install
   ```

4. Configure Environment Variables:
   
     - **Configure Spring Boot application.yml for database and other configurations.**

5. Run the Application:
    - **Start the frontend:**
    ```bash
    npm run dev
    ```

    - **Start the backend:**
    ```bash
    mvn spring-boot:run
    ```

6. Open your browser and visit `http://localhost:5173` to access the application.

   Log in using one of the following credentials or register a new account:

   - **Admin:**
     - Username: `admin@gmail.com`
     - Password: `admin`
   
   - **User:**
     - Username: `customer@gmail.com`
     - Password: `12345678`
   
   - **Seller:**
     - Username: `seller@gmail.com`
     - Password: `12345678`

7. Screenshots

- **User Page**

![ss_homepage](https://github.com/user-attachments/assets/6c5541ee-d35e-4139-aa36-cb42ee00931d)

![ss_loginpage](https://github.com/user-attachments/assets/238ddffc-bb10-4a4c-a1ce-a7286f2cda90)

![ss_productdetailpage](https://github.com/user-attachments/assets/eaf7ea07-b9b6-4fbb-875c-1d214fa54f77)

![ss_cartpage](https://github.com/user-attachments/assets/e4e80a7c-ea14-45f5-9e99-1c9d6c67f21c)

![ss_checkoutpage](https://github.com/user-attachments/assets/b13fbac0-1ae2-4c61-b2e8-c44ba4b62006)

![ss_orderpage](https://github.com/user-attachments/assets/c0b6ff27-4a5a-4b11-9998-3a6966ed3e10)

![ss_myaccountpage](https://github.com/user-attachments/assets/559703be-0136-459c-85a9-1d9729c662b8)

![ss_favoritespage](https://github.com/user-attachments/assets/19207f12-4c79-4a0d-9e65-d3676c8ce65b)

![ss_followingpage](https://github.com/user-attachments/assets/fdf02b8b-d0ff-4192-8f4d-ac4432828836)

![ss_storepage](https://github.com/user-attachments/assets/b7561b8d-950a-475e-9532-4fe3da04ca53)

- **Seller Page**

![ss_sellerhome](https://github.com/user-attachments/assets/5c584993-0350-4949-afd1-cadf1b4c0f4a)

![ss_sellerorderpage](https://github.com/user-attachments/assets/d3e278d0-76f2-49c2-a398-680c5e8c2e11)

![ss_myproductpage](https://github.com/user-attachments/assets/ae6313a0-9642-40a8-b4c5-745fc586b6be)

![ss_newproductpage](https://github.com/user-attachments/assets/b0ba433d-6010-4e9e-9671-6e1dc1e0e125)

![ss_reviewmanagementpage](https://github.com/user-attachments/assets/8ba770b8-ab37-4faa-bae6-4ce42f3fe19c)

- **Admin Page**

![ss_adminhomepage](https://github.com/user-attachments/assets/37d2bfe4-8802-4786-ba59-4399daf9e301)

![ss_adminshoplist](https://github.com/user-attachments/assets/89e6237d-baf9-4ffc-8302-3f40f9cb303b)

![ss_adminuserlist](https://github.com/user-attachments/assets/13026691-cf5a-40e0-a97a-0ff66715eebb)

![ss_admincategorylist](https://github.com/user-attachments/assets/00c85496-3497-403c-b663-b20ebb153255)




