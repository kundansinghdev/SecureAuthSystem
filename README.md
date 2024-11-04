
# 🔒 SecureAuthSystem

SecureAuthSystem: An all-in-one user authentication solution that combines simplicity with versatility. Easily register and enjoy flexible login options—choose to sign in via 🧑‍💻 username, ✉️ email, or 📱 phone number. Designed for secure, seamless access at your convenience.

---

## Endpoints

### 1. User Registration
- **Endpoint**: `POST http://serverxyz:9090/auth/signup`
- **Description**: Registers a new user with essential details.
- **Request Body**:
  ```json
  {
    "username": "kundansingh",
    "password": "Kundan*&^$@!!@#321",
    "mobileNumber": "7827843470",
    "email": "kundangetcode@gmail.com"
  }
  ```

---

### 2. Login with Username and Password
- **Endpoint**: `POST http://serverxyz:9090/auth/login`
- **Description**: Authenticates a user with their username and password.
- **Request Body**:
  ```json
  {
    "username": "Kundansingh",
    "password": "Kundan*&^$@!!@#321"
  }
  ```

---

### 3. OTP Verification for Mobile
- **Endpoint**: `POST http://serverxyz:9090/auth/verify-otp`
- **Description**: Verifies the OTP sent to the user’s mobile number.
- **Request Body**:
  ```json
  {
    "mobileNumber": "7827843470",
    "otp": "526221"
  }
  ```

---

### 4. Login with Mobile Number
- **Endpoint**: `POST http://serverxyz:9090/auth/login-with-mobile`
- **Description**: Initiates OTP-based login using a mobile number.
- **Request Body**:
  ```json
  {
    "mobileNumber": "7827843470"
  }
  ```

---

### 5. Login with Email
- **Endpoint**: `POST http://serverxyz:9090/auth/login-with-email`
- **Description**: Initiates OTP-based login using an email address.
- **Request Body**:
  ```json
  {
    "email": "kundangetcode@gmail.com"
  }
  ```

---

### 6. OTP Verification for Email
- **Endpoint**: `POST http://serverxyz:9090/auth/verify-email-otp`
- **Description**: Verifies the OTP sent to the user’s email address.
- **Request Body**:
  ```json
  {
    "email": "kundangetcode@gmail.com",
    "otp": "413349"
  }
  ```

---

## Database Structure

**Database Name**: `SecureAuthSystem`

---

### Tables

#### User_Credentials
Stores user information, including login credentials.
```sql
CREATE TABLE User_Credentials (
    username VARCHAR(20) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    mobile_number VARCHAR(15) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE
);
```

#### Category
Defines unique categories for the system.
```sql
CREATE TABLE Category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);
```

#### Subcategory
Stores subcategories and links them to categories for organizational hierarchy.
```sql
CREATE TABLE Subcategory (
    subcategory_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);
```

---
