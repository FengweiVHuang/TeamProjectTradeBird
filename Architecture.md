# UBC Second-Hand Trading Platform Architecture

This document outlines the architecture for the UBC Second-Hand Trading Platform using the Model-View-Controller (MVC) and Client-Server design principles.

## Component Descriptions

### 1. Models

#### **UserModel**
- **Responsibility:**  
  Manages user data, including registration, login, and authentication. Stores user profiles, transaction records, and trust scores.
- **Location:**  
  Server-side.
- **Communications:**  
  - Communicates with `UserController` to provide and validate user data.  
  - Interfaces with the database to store and retrieve user information.

#### **ProductModel**
- **Responsibility:**  
  Stores and manages product data, including name, category, price, description, images, and tags.  
- **Location:**  
  Server-side.
- **Communications:**  
  - Communicates with `ProductController` to provide and update product information.  
  - Interfaces with the database to store product details.

---

### 2. Controllers

#### **UserController**
- **Responsibility:**  
  Handles user registration, login, authentication, and profile management.  
- **Location:**  
  Server-side.
- **Communications:**  
  - Communicates with `UserModel` to retrieve and validate user information.  
  - Communicates with `UserView` to send authentication and profile data back to the client.

#### **ProductController**
- **Responsibility:**  
  Manages product posting, editing, deletion, and query requests.  
- **Location:**  
  Server-side.
- **Communications:**  
  - Interacts with `ProductModel` to fetch or update product information.  
  - Communicates with `ProductView` to send product data to the client.

---

### 3. Views

#### **UserView**
- **Responsibility:**  
  Displays interfaces for user registration, login, and profile management.  
- **Location:**  
  Client-side.
- **Communications:**  
  - Sends user requests (e.g., login or registration) to `UserController`.  
  - Displays server responses, such as login status or error messages, to the user.

#### **ProductView**
- **Responsibility:**  
  Displays product listings, details, and posting/editing interfaces.  
- **Location:**  
  Client-side.
- **Communications:**  
  - Sends requests to `ProductController` for querying or posting products.  
  - Displays product data and status updates received from the server.
