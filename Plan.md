# Project Plan

## 1. Project Coordination

1. **Project Manager Responsibilities**: The Project Manager is responsible for task allocation, progress tracking, conflict resolution, and ensuring smooth communication among team members to keep the project on track. However, the Designer also plays a crucial role, as they handle essential tasks such as server setup, database management, and task assignment. Beyond system design, the Designer drives the technical implementation, ensuring the project’s technical requirements are met efficiently.

2. **Project Management Strategies**:
   - Set clear goals and scope.
   - Create a detailed schedule with key milestones.
   - Allocate resources effectively, ensuring clear direction for the project.
   - During execution, assign tasks, establish a unified workflow, and document progress in real-time to maintain control.
   - In monitoring, regularly review progress, assess quality, identify potential risks, and ensure the project follows the plan.
   - Encourage team communication and collaboration through regular meetings, shared management software, and feedback mechanisms, addressing issues promptly to optimize the process.

3. **Meeting Schedule**: Typically, 2-3 meetings are held weekly, with agendas set by the Project Manager. In case of urgent issues, the Designer may also call ad hoc meetings with developers to resolve immediate problems and avoid further complications.

## 2. Communication Tool

We primarily use **Discord** for team communication, as it saves time by eliminating the need to send meeting links. Team members can directly join discussions within the same server. Additionally, Discord's screen-sharing feature facilitates brainstorming using Canva’s shared whiteboard.

## 3. Members of components in the architecture:

The designer, **Yiran Wang**, is responsible for the overall architecture of our project, ensuring that all components are functional and correct. While Yiran oversees the main development, other team members also contribute to various components as needed.

# Project Timeline and Milestones

| **Dates**         | **Milestone**                              | **Description**                                                                                                     | **Planned Contingency Measures**                                                                                  |
|------------------|-----------------------------------------|-------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| **10/28 - 10/31** | Project Initiation & Requirement Confirmation | Confirm project goals and requirements, decompose functional modules, and create a detailed requirement document. | Record discussions if members are absent and send a summary to everyone.                                                        |
| **11/1 - 11/4**   | Interface Design & Prototype Development    | Complete platform interface design, including homepage, product detail page, and seller interface. Develop a low-fidelity prototype to ensure the design meets user needs. | If there are delays, simplify the design and use alternative tools (e.g., Canva).                                                 |
| **11/5 - 11/8**   | Database & Server Architecture Setup        | Start setting up the database and server to handle user registration, login, product listing, and transaction records. | Use a local test server first if there are configuration issues.                                                                |
| **11/9 - 11/12**  | User Registration & Login System Development | Implement and test user registration and login functionality, including UBC student verification and email validation. | Test a simplified version if code progress is slow to ensure basic functions are implemented.                                                  |
| **11/13 - 11/16** | Product Posting & Categorization Feature Development | Implement product posting functionality with price, condition, and category options. Enable sellers to upload and update product details and organize products by category. | Reassign work if team members are absent to ensure steady progress.                                                       |
| **11/17 - 11/19** | Product Detail Page Development             | Design and develop the product detail page, including features to display product information, images, and a discussion area for buyers and sellers. | Focus on core details first and delay enhancements if progress slows.                                                     |
| **11/20 - 11/22** | Discussion Feature & Buyer Preference Development | Develop a discussion system on product detail pages where buyers and sellers can interact. Implement a buyer preference feature to recommend products based on browsing and selection behavior. | Prioritize discussion features and delay buyer preference recommendations if necessary.                                                   |
| **11/23 - 11/26** | User Testing & Feedback Collection          | Conduct small-scale user testing, collect feedback, and fix issues to ensure the platform meets user experience and security standards. | Record feedback manually if tool limitations hinder collection.                                                             |
| **11/27 - 11/29** | System Refinement & Function Optimization    | Optimize the platform based on user feedback, fix known issues, and improve system stability and responsiveness. | Focus on critical bug fixes and delay minor enhancements if required.                                                             |
| **12/1 - 12/3**   | Final Testing & MVP Launch                  | Conduct full-system testing and release the MVP after verifying no major issues. | Exclude non-critical features if necessary to ensure a timely launch.                                           |

---

# Acceptance Testing Plan

### 1. User Interface Testing

**Homepage**  
- **Verification:** Test the search function to ensure the following filters work:
  - Category
  - Price range  
- **Test Cases:** Input various filters and verify results.  
- **Passing Criteria:** All filters work independently and in combination without errors.  

**Product Detail Page**  
- **Verification:** Test if item details (price, seller info, images) display correctly.  
- **Test Cases:** Check if all details load properly.  
- **Passing Criteria:** All details are accurate and accessible.

**Seller Interface**  
- **Verification:** Ensure sellers can post and edit product details.  
- **Test Cases:** Save and retrieve product data from the database.  
- **Passing Criteria:** Data saving and retrieval work as expected.

---

### 2. Functional Testing

**Registration and Login**  
- **Verification:** Test UBC email authentication.  
- **Test Cases:** Test various valid/invalid inputs.  
- **Passing Criteria:** Only UBC emails are accepted.  

**Discussion and Messaging**  
- **Verification:** Test the discussion area for posting and replying to comments. Ensure private messaging between users functions as intended.  
- **Test Cases:** Post a comment, reply to a comment, and send private messages to other users.  
- **Passing Criteria:** Comments and messages appear immediately without errors and are accessible to the intended recipient.

---

### 3. Security Testing

**Email Verification**  
- **Verification:** Test UBC email verification and code expiration.  
- **Passing Criteria:** Only valid UBC emails are accepted.

**Data Protection**  
- **Verification:** Ensure SSL encryption for all sensitive data.  
- **Passing Criteria:** All data transmissions are encrypted securely.

---

### 4. Performance and Load Testing

- Simulate multiple user interactions, such as simultaneous commenting and messaging.  
- Measure page load times under different loads.

---

### 5. User Feedback Integration

**Testing Feedback:**  
- Collect feedback during testing and prioritize fixes.

**Final Review:**  
- Conduct a team review to ensure all critical functionalities are included before launch.

---

This updated timeline and testing plan reflect the new focus on using the discussion and messaging features for buyer-seller transactions while removing the "Want Buy" functionality. The project is structured to ensure timely delivery of a robust and user-friendly MVP. 
