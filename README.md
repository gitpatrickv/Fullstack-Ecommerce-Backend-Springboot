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

