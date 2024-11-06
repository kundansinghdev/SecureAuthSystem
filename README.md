

# 🔒 SecureAuthSystem Documentation

SecureAuthSystem provides a robust authentication framework allowing users to register and log in using multiple options. This solution combines security and flexibility for seamless user access.

---

## 📌 API Endpoints

### 1. **User Registration**
   - **Endpoint**: `POST http://serverxyz:9090/auth/signup`
   - **Description**: Registers a new user by collecting essential details.
   - **Request Body**:
     ```json
     {
       "username": "kundansingh",
       "password": "Kundan*&^$@!!@#321",
       "mobileNumber": "7827843470",
       "email": "kundangetcode@gmail.com"
     }
     ```

### 2. **Login with Username and Password**
   - **Endpoint**: `POST http://serverxyz:9090/auth/login`
   - **Description**: Authenticates a user using their username and password.
   - **Request Body**:
     ```json
     {
       "username": "Kundansingh",
       "password": "Kundan*&^$@!!@#321"
     }
     ```

### 3. **OTP Verification for Mobile**
   - **Endpoint**: `POST http://serverxyz:9090/auth/verify-otp`
   - **Description**: Verifies OTP sent to the user’s mobile.
   - **Request Body**:
     ```json
     {
       "mobileNumber": "7827843470",
       "otp": "526221"
     }
     ```

### 4. **Login with Mobile Number**
   - **Endpoint**: `POST http://serverxyz:9090/auth/login-with-mobile`
   - **Description**: Initiates OTP-based login using a mobile number.
   - **Request Body**:
     ```json
     {
       "mobileNumber": "7827843470"
     }
     ```

### 5. **Login with Email**
   - **Endpoint**: `POST http://serverxyz:9090/auth/login-with-email`
   - **Description**: Initiates OTP-based login using an email.
   - **Request Body**:
     ```json
     {
       "email": "kundangetcode@gmail.com"
     }
     ```

### 6. **OTP Verification for Email**
   - **Endpoint**: `POST http://serverxyz:9090/auth/verify-email-otp`
   - **Description**: Verifies the OTP sent to the user’s email.
   - **Request Body**:
     ```json
     {
       "email": "kundangetcode@gmail.com",
       "otp": "413349"
     }
     ```

### 7. **Password Reset via Email**
   - **Endpoint**: `POST http://localhost:9090/auth/ForgetPasswordwithemail`
   - **Description**: Initiates the password reset process by sending a reset link or OTP to the email.
   - **Request Body**:
     ```json
     {
       "email": "kundangetcode@gmail.com"
     }
     ```

### 8. **Reset Password**
   - **Endpoint**: `POST http://localhost:9090/auth/reset-password`
   - **Description**: Allows the user to reset their password using a provided OTP.
   - **Request Body**:
     ```json
     {
       "email": "kundangetcode@gmail.com",
       "otp": "784461",
       "newPassword": "NewSecurePassword123"
     }
     ```

---

## 🗄️ Database Structure

**Database Name**: `SecureAuthSystem`

### 1. **User_Credentials Table**
   - Stores basic user information and login credentials.
   - **SQL**:
     ```sql
     CREATE TABLE User_Credentials (
         username VARCHAR(20) NOT NULL PRIMARY KEY,
         password VARCHAR(100) NOT NULL,
         mobile_number VARCHAR(15) NOT NULL,
         email VARCHAR(50) NOT NULL UNIQUE
     );
     ```

### 2. **Category Table**
   - Defines unique categories.
   - **SQL**:
     ```sql
     CREATE TABLE Category (
         category_id INT PRIMARY KEY AUTO_INCREMENT,
         name VARCHAR(255) NOT NULL UNIQUE
     );
     ```

### 3. **Subcategory Table**
   - Stores subcategories linked to specific categories for hierarchy.
   - **SQL**:
     ```sql
     CREATE TABLE Subcategory (
         subcategory_id INT PRIMARY KEY AUTO_INCREMENT,
         category_id INT,
         name VARCHAR(255) NOT NULL,
         FOREIGN KEY (category_id) REFERENCES Category(category_id)
     );
     ```
