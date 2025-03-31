# Design Specification

## Problem Description (Problem)

### Background
Second-hand trading is an integral part of student life at UBC, helping students save money and obtain essential items. However, current second-hand trading platforms present multiple challenges, such as safety risks, inefficiency, and high logistics costs. These platforms fail to meet the specific needs of a campus community, leading to a lack of trust and transparency among users.

### Importance and Impact
- **Lack of Security**: User identities on existing platforms cannot be verified, increasing the risk of scams and false information.
- **Inefficiency**: Students spend significant time filtering irrelevant information to find what they need.
- **High Logistics Costs**: Off-campus transactions incur high shipping fees.
- **Weak Sense of Community**: Current platforms do not promote interaction between buyers and sellers, resulting in isolated and untrustworthy trading experiences.

---

## Solution
Our UBC-exclusive second-hand trading platform will address these problems through the following core features:

### Core Features
1. **User Identity Verification**
   - **Description**: Access is restricted to UBC students, verified via their UBC email addresses.
   - **Benefit**: Ensures both buyers and sellers are campus members, reducing external risks and creating a secure network.

2. **Search and Filtering**
   - **Description**: Provides a search bar and filters for price range and category selection.
   - **Benefit**: Helps students quickly find relevant items, enhancing efficiency.

3. **Product Detail Page**
   - **Displayed Information**: Item name, price, seller name, description, image, and tags.
   - **Discussion Area**: Allows buyers and sellers to interact on the product page, with comments visible to others for better judgment.

4. **Seller Interface**
   - **Description**: Sellers can input the item name, price, and select category tags. Image uploads are supported for better product presentation.

---

## User Interface and Layout

### Navigation Bar
- **Contents**: Includes options like "Homepage" and "Create New Post."

### Homepage
- **Layout**: Features a search bar, price range selector, and category tags for filtering.
- **Scrollable Panel**: Displays brief item information, including name, price, and tags.

### Product Detail Page
- **Primary Features**:
  - Displays detailed product information, such as name, price, seller name, description, and images.
  - Includes a discussion area where buyers and sellers can leave comments or questions.

### Seller Interface
- **Posting Process**:
  - Sellers input item name, price, and select category tags.
  - Images can be uploaded.
  - Includes a preview option before posting.
---

## Interaction Design

### Buyer Perspective
1. **Filter Items**:
   - Click on the category tags at the top (e.g., "Furniture") to filter the item list. The list dynamically updates to show only items matching the selected category.

2. **View Item Details**:
   - Click on an item card to enter the product detail page.
   - On the detail page, view the product information and comment section.
   - Interact with the seller through the comment section or leave contact information in the comments.

3. **Comment on Items**:
   - Enter questions or suggestions in the comment section, such as: "Can you lower the price for this item?"
   - Click "Reply" to further communicate with other buyers or the seller.

### Seller Perspective
1. **Post Items**:
   - Fill in the item name, price, and category tags on the "New Post" page and upload images.
   - Preview the post to confirm the details before clicking the "Post" button to publish.

2. **Interact with Buyers**:
   - Respond to buyer inquiries in the comment section on the product detail page, such as item condition or pickup arrangements.
   - Provide contact information in the comments for follow-up transactions.

---
## Error Handling
- **Form Validation**:
  - **Required fields**: If left empty, prompt users with "This field is required."
  - **Invalid inputs**: For example, if an email is not a UBC email, display "Please use a valid UBC email address."
  - 
---

## Backend Architecture

### Server-Side
- **Core Functions**: User registration, login, product posting, and comment handling.
- **Database Structure**:
  - **Users Table**: Stores user information, including verified email and profile details.
  - **Products Table**: Contains product listings with fields for name, price, category, images, and unique identifiers.
  - **Comments Table**: Handles discussion area content for product pages.

### Security Measures
1. **SSL Encryption**: Ensures all data transferred between clients and the server is secure.
2. **Email Verification**: Automatically sends verification links to UBC emails upon registration.
3. **Password Protection**: Implements hashed passwords for secure storage.
---

# Design Document

You can find the detailed layout sketch PDF version of the design specification here [Design.pdf](./Design.pdf)

## Conclusion

By implementing this design, we will deliver a Minimum Viable Product within 4 weeks, providing UBC students with a secure, efficient, and community-driven second-hand trading platform.
