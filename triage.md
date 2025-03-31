# Triage 

## Priority Issues

### 1. Issues to be Resolved (Valid Issues)

#### **Critical Functional Errors (Highest Priority)**
1. **Post Creation Timestamp Error**  
   - **Description**: Posts display future timestamps (e.g., "2024/12/4 at 17:32"), affecting data reliability.  
   - **Priority**: P0 - Emergency  
   - **Resolution**: Fix the timestamp logic to ensure posts display the correct creation time.
   - **Partly Solved**
   - **Causes**: The time zone of the database did not match current location.

2. **Large Image Upload Bug**  
   - **Description**: Uploading large images (e.g., 10000x10000 pixels) causes an error message, but the post is still created. Pressing submit multiple times creates duplicate posts.  
   - **Priority**: P0 - Emergency  
   - **Resolution**: Validate image size on both frontend and backend, and ensure proper error handling to prevent duplicate submissions.
   - **Solved**

#### **High Priority Issues**
3. **Tab Refresh Issue**  
   - **Description**: Clicking tabs briefly refreshes the page, logging the user out and changing the "Profile" button to "Login."  
   - **Priority**: P1 - High  
   - **Resolution**: Debug session handling logic to prevent unintended logout during navigation.
 
4. **Post Content Too Long Causes Failure (Identified Issues Previously Overlooked by the Review Team)**  
   - **Description**: Posts with excessively long content sometimes fail to publish.  
   - **Priority**: **P1 - High**  
   - **Resolution**: Define maximum content length and notify users when it is exceeded.

#### **Medium Priority Issues**
5. **Reply Scrolling Bug**  
   - **Description**: Long replies scroll off-screen into empty space, disrupting user experience.  
   - **Priority**: P2 - Medium  
   - **Resolution**: Implement scrolling constraints to keep content within viewable bounds.

6. **Price Decimal Precision Issue**  
   - **Description**: Prices can be set with excessive decimal places, causing formatting inconsistencies.  
   - **Priority**: P2 - Medium  
   - **Resolution**: Limit price input to two decimal places in the UI and validate on the backend.
   - **Solved**
   - **Causes**: Only checked the price in frontend. And due to the accuracy for float, JS will regard something like 0.9999999999 as 1. Which make the check unsafe.


---

### 2. Issues Not to Be Resolved (Invalid Issues)

1. **`npm tests` Only Runs One Test**  
   - **Reason**: We conducted manual testing using Postman for backend validation. These tests were not integrated into the `npm tests` suite.  
   - **Action**: No fix required. Update documentation to clarify the testing method.

2. **Registration Error: "localhost undefined"**  
   - **Reason**: This error occurs because React does not connect to the backend when run locally.  
   - **Action**: No fix required. Provide instructions to run the `.jar` file or access the website directly.

---

## Priority Explanation
### **P0 - Emergency**
1. **Post Creation Timestamp Error**: Directly affects data integrity, critical for user trust.  
2. **Large Image Upload Bug**: Causes both functional errors and data duplication, significantly impacting system stability.

### **P1 - High Priority**
3. **Tab Refresh Issue**: Disrupts navigation and session continuity, creating a poor user experience.  
4. **Post Content Too Long Causes Failure**: Impacts core post creation functionality, requiring resolution to ensure reliability.

### **P2 - Medium Priority**
5. **Reply Scrolling Bug**: Minor disruption in user experience but does not block critical functionality.  
6. **Price Decimal Precision Issue**: A formatting issue with minimal user impact, suitable for future milestones.

---

## Summary of Actions
- Resolve all **valid issues** based on assigned priorities.  
- For **invalid issues**, provide clear explanations and update documentation to avoid misunderstandings.  
- Continuously review and improve documentation and error-handling mechanisms.
