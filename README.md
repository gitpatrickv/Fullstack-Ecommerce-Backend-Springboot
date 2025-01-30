# Note
My code here is a bit messy, but I’ve already refactored it. I tried a different approach, it's still a work in progress, but take a look!
- **Frontend URL:** https://github.com/gitpatrickv/fullstack-react-ecommerce-remake
- **Backend URL:** https://github.com/gitpatrickv/fullstack-springboot-ecommerce-remake

## Shopee E-Commerce Platform Clone

## Introduction
Welcome to the Shopee E-Commerce Platform Clone! This project is an imitation of the popular e-commerce platform, Shopee, designed to replicate its core features and functionalities. The goal of this project is to provide a comprehensive, full-stack implementation that covers everything from user authentication, product management, and shopping cart features, to payment processing, order management, product rating, store service rating, the ability to follow your favorite stores and real-time chat functionality.

The project utilizes React and TypeScript for the frontend, with Chakra UI for styling and Zustand for state management. The backend is powered by Java 17 and Spring Boot, ensuring secure login through Spring Security with JWT-based authentication.

## Features

- **User Authentication & Authorization:** Secure sign-up, login, and role-based access control (admin, seller, and buyer), with personalized pages for buyers, sellers, and admins.
- **Product Management:** Create, update, delete, and manage product listings with support for multiple categories, sizes, and colors.
- **Search & Filtering:** Advanced search capabilities with filters.
- **Shopping Cart & Checkout:** Seamless shopping cart experience with options for payment via Stripe or cash on delivery.
- **Order Management:** Track order history, and view delivery status.
- **Product Rating & Store Service Rating:** Rate products and store service to provide feedback and enhance user experience.
- **Real-Time Chat:** Instant messaging between users and sellers using WebSockets.

## Tech Stack

- **Frontend:** React, TypeScript, Chakra UI, React Query, Zustand
- **Backend:** Spring Boot, Java 17, MySql
- **Authentication:** Spring Security, JWT
- **Payments:** Stripe API integration
- **DevOps:** Docker

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

3. Run the Application:
    - **Start the frontend:**
    ```bash
    npm run dev
    ```

    - **Start the backend:**
    ```bash
    docker compose up
    ```

4. Open your browser and visit `http://localhost:5173` to access the application.

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


5. Screenshots & Video
- **Video**

https://youtu.be/L5btxVQpZRM

- **User Page**

![home](https://github.com/user-attachments/assets/1d287039-8ae2-45a6-9782-3dc30aa03e9f)

![chat](https://github.com/user-attachments/assets/268ea0be-bdcd-46b1-8252-55bef27c250e)

![productdetail](https://github.com/user-attachments/assets/9fd5e7ce-ddbe-4032-b23c-2da66bd743d7)

![cartpage](https://github.com/user-attachments/assets/d8609481-ea66-417a-9109-603c0176f372)

![checkoutpage](https://github.com/user-attachments/assets/d31b1520-ea8e-42fc-8300-6a5f0bba35c9)

![storepage](https://github.com/user-attachments/assets/f517d4c5-b818-4519-8826-94834c8aa82f)

![searchpage](https://github.com/user-attachments/assets/987794b6-b988-4030-a013-98bd3d1dbce9)

![categorypage](https://github.com/user-attachments/assets/c56eeae7-9ada-4219-80fe-2b84860cbcbb)

![accountpage](https://github.com/user-attachments/assets/749bbf01-8b39-49e2-a6e7-8f0211e05712)

![changepasspage](https://github.com/user-attachments/assets/3089665d-e1c1-4797-b2a0-d8c6e7680d2b)

![favoritespage](https://github.com/user-attachments/assets/71beab54-16c0-4f22-a79d-420aa1a7d521)

![followingpage](https://github.com/user-attachments/assets/10447925-c185-4bb6-9292-cf8d01c57993)

![mypurchasepage](https://github.com/user-attachments/assets/50349b8f-858e-49e5-a443-4b5958cec1ad)

- **Seller Page**

![loginpage](https://github.com/user-attachments/assets/b9bd7020-9b7e-4cf4-a221-55e0111e11b9)

![sellerspage](https://github.com/user-attachments/assets/c1c994ce-d52e-4e27-8fe6-5a0a8360dfef)

![soldout](https://github.com/user-attachments/assets/53e9709b-4615-4a22-8a1e-4bae4cf4a964)

![myproductpage](https://github.com/user-attachments/assets/7e9e940f-16fe-48e1-af80-20b97410d176)

![newproductpage](https://github.com/user-attachments/assets/84205a29-bc7c-4ac3-a663-57040f549521)

![reviewpage](https://github.com/user-attachments/assets/011b8e9a-0316-4ac3-84d0-d43e32e0c2ef)

![allorderpage](https://github.com/user-attachments/assets/71efe56c-9e2e-495e-9809-ec76283af893)

![shopprofilepage](https://github.com/user-attachments/assets/8c47b6a5-853a-4520-8165-7fc53fa83874)

- **Admin Page**

![adminpage](https://github.com/user-attachments/assets/9b764322-da5f-4364-ba8e-c757f9e883aa)

![userlist](https://github.com/user-attachments/assets/157869dc-156d-4efb-b854-53be1ffb1ac3)

![shoplist](https://github.com/user-attachments/assets/98bef262-eaed-42e0-a1e0-7d7a52a23221)

![categorylist](https://github.com/user-attachments/assets/95288aeb-4cd3-4c58-9c37-1151cd0a9b1a)





