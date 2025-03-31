# Evaluation of `project-ivorwen-auto-cal`

## Issues List
### 1. Lack of Frontend-Backend Interaction
- **Description**:
  - The login page sends requests, but no response is captured from the backend.
  - Missing API calls make dynamic functionality (e.g., fetching data from the database) impossible.

- **How We Found It**:
  - While testing the login functionality, we inspected the Network tab in the browser developer tools and found no responses to the login requests.

### 2. Frontend Design Issues
- **Description**:
  - The `hwlist.html` page lacks CSS styling, making it inconsistent with the login page and reducing user experience quality.

- **How We Found It**:
  - We statically analyzed the HTML files in the project and found that the `hwlist.html` file does not reference any CSS files.

### 3. Backend Issues
- **Description**:
  - The backend fails to map request addresses correctly, causing login functionality to fail.
  - `.py` files throw errors during execution, indicating missing configurations or dependencies.

- **How We Found It**:
  - By running the `.py` files in the command line or an IDE, we encountered runtime errors suggesting missing functions or modules.

### 4. Data Security Issues
- **Description**:
  - Passwords are stored in plaintext in the database, which poses a significant security risk.

- **How We Found It**:
  - By inspecting the database contents (e.g., SQL query results), we noticed that user passwords are stored in plaintext without any encryption.

### 5. Demo Files Issues
- **Description**:
  - All project web pages are static and cannot fetch or display dynamic data from the server.

- **How We Found It**:
  - We reviewed the demo files and confirmed that they lack dynamic data-fetching logic.

### 6. Environment Setup Issues
- **Description**:
  - The project documentation does not clearly state whether additional dependencies or environment setups are required, making the project difficult to run.

- **How We Found It**:
  - When attempting to run the project, we encountered errors and found no detailed environment setup instructions in the documentation.

## Suggestions for Improvement
1. **Frontend-Backend Communication**: Fix API request mappings to ensure proper functionality, such as user authentication.
2. **Frontend Design**: Add CSS styling to all pages for consistency and better user experience.
3. **Data Security**: Encrypt sensitive data like passwords as soon as possible.
4. **Documentation**: Include a clear guide for setting up and running the project with all dependencies.
