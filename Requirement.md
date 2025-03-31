# UBC Second-Hand Trading Platform Detailed Requirements

## Navigation Bar Requirements
1. The system must provide a navigation bar with the following options:
   - **About Us**
   - **Home**
   - **New Post**
   - **All Items**
   - **My Items**
   - **Want Buy**
   - **User Profile**
2. The navigation bar must display the currently logged-in user's username on the top-right corner when the user is logged in, to indicate the current login status.
3. The system must display a "Log Out" option when the user clicks on their username.

---

## Filtering Requirements
4. The system must provide item tags displayed below the navigation bar.
5. Users must be able to select a price range and tags to filter items by category (e.g., Furniture, Books).
6. Users must be able to select multiple tags as well as a price range at once for combined filtering.
7. After selecting tags and/or price range, the system must display only items matching the selected requirements.
8. If no tags or price range are selected, the system must display all items.
9. Filtered results must dynamically update without requiring server redeployment.

---

## Item List Requirements
10. The system must display the following details for each item in the item list:
    - Item name
    - Item price
    - Item tags
11. Each item in the list must have a unique identifier for loading detailed information.
12. Item prices must be prefixed with the `$` symbol with two decimal places (e.g., `$20.99`).
13. The item list must support vertical scrolling to display multiple items.
14. The selected item is centered on the scroll bar on the left side of the page when chosen by the user to view detailed information.

---

## Item Details Page Requirements
15. The item details page must display the following information:
    - Item name
    - Item price
    - Item description
    - Seller's username (prefixed with "User:")
    - Item tags
    - Item images
    - Post date (formatted as `YYYY/MM/DD HH:mm`).
16. The details page must include a discussion area for communication between buyers and sellers.
17. The discussion area must support two modes:
    - **Public Mode**: Comments are visible to all users.
    - **Private Mode**: Comments are visible only to the buyer and the commenter, enabling them to exchange contact information for offline transactions.
18. Users must be able to enter comments in the discussion area.
19. The comment input box must limit the content to 200 characters.
20. Users must be able to submit comments by clicking the "Submit" button.
21. Submitted comments must appear immediately in the discussion area.
22. Each comment must display the following:
    - Commenter's username
    - Comment content
    - Timestamp of the comment.
23. The discussion area must allow buyers and sellers to exchange contact information for offline transactions.

---

## Favorites and Buyer Interest Requirements
24. The system must provide an "Add to Favorites" button on the item details page to allow users to mark items they are interested in.
25. The system must provide an "Unfavorite" button for removing items from the favorites list.
26. The system must update the favorites list immediately without requiring a page refresh.

---

## Post Management Requirements
27. Sellers must be able to delete their posted items after they are sold or no longer available.
    - The "Delete Post" button must be available on the **My Items** page for each item posted by the seller.
    - Upon deletion, the item must be removed from the **All Items** list and **Want Buy** lists of any users.
    - The system must display a confirmation message, such as "Post deleted successfully," upon successful deletion.
    - If the deletion fails, the system must display an error message, such as "Removal failed."

---

## Password Management Requirements
28. The system must allow users to reset their passwords when forgotten, by requesting a reset link to be sent to their registered email address.
29. The password reset link must expire after **10 minutes**.
30. Users must be able to set a new password after clicking the reset link, which should navigate them to a secure page.
31. After resetting the password, the system must display a success message to the user.

---

## Home Page Requirements
32. The home page should display a welcome message and an "About Us" section.
33. The "About Us" section must include a static description that introduces the team and its purpose.
34. The home page must provide a logout button, as shown in the design.
35. Only the home page and "About Us" page should be visitable when the user is not logged in.

---

## Responsive Design Requirements
36. The system must ensure that all input fields, buttons, pictures, and lists display correctly on all normal screen resolutions.

---

## Error Handling Requirements
37. The system must display an error message if loading item lists or details fails.
38. If a user submits an empty comment, the system must display an error message.
39. If an uploaded image format is unsupported, the system must display an error message, such as:
    - "Invalid image format. Supported formats are: .jfif, .pjpeg, .jpeg, .pjp, .jpg, .png."

---

## Additional Requirements
40. Users must be able to delete their posted items from their profile page.
41. The system must provide a summary view displaying all items currently being sold by a specific user.
42. When sellers upload images for items, the system must validate that the image format matches one of the supported formats (.jfif, .pjpeg, .jpeg, .pjp, .jpg, .png). Unsupported formats must prompt an error message, preventing upload.
43. If the user tries to visit a details page and is redirected to the login page, the system must navigate the user to the intended page after login.

---

## Backend Requirements
44. Record essential user information (login information, items selling, items liked) and any possible errors.
45. Encrypt user passwords for security concerns.

---

## Summary
This document lists all detailed requirements for the UBC second-hand trading platform. It replaces search-based functionality with tag-based filtering and emphasizes discussion-based offline transactions between buyers and sellers. Sellers are also provided with the ability to delete their posts after the item is sold or no longer available. An "About Us" section is included as a static webpage on the home page to introduce the platform's purpose.
