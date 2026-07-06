# Xpress (formerly Hero Money)

Xpress is a secure loan application and processing API platform built with **Spring Boot 3**. It provides structured workflows for customers to apply for loans and for loan officers to review, manage, and generate loan agreements.

## Features

### 🔐 Authentication & Security
* **JWT Authentication**: Secure stateless authentication using Access and Refresh tokens.
* **Role-Based Access Control (RBAC)**: Distinguishes between `CUSTOMER` and `LOAN_OFFICER` roles.
* **IDOR Protection**: Service and Controller endpoints are hardened to verify resource ownership, preventing customers from accessing or signing other users' loans/agreements.
* **OTP Tokens**: Email verification tokens for registration and access initialization.

### 👤 Customer Profile & Document Verification
* **Stepwise Onboarding**: Customers initialize registration via email verification, followed by profile completion (age, salary, company, address details).
* **Robust File Validation**: Supports pay slips, account statements, and identity documents uploaded to **Cloudinary**:
  * Centralized size validation (maximum **5MB**).
  * Allowed formats: `JPEG`, `PNG`, and `PDF` only.
* **Address Constraints**: Complete address profiles are validated (requires valid city and zipCode).

### 💵 Loan Application Flow
* **Loan Submission**: Customers apply for loans specifying loan purpose, positive amounts, and positive terms.
* **Agreement Signing**: When an officer approves a loan and generates an agreement, the customer can view and securely decide (`ACCEPT` or `DECLINE`) on the agreement.

### 👔 Loan Officer Workflows
* **Invite-only Registration**: Officers register via internal invite links.
* **Assessment & Rates**: Officers evaluate applications, assign interest rates, and generate binding agreements.

---

## Technical Stack
* **Language/Framework**: Java 17+ / Spring Boot 3
* **Database**: PostgreSQL (validated and managed via Flyway Migrations)
* **Email Service**: Brevo (Sendinblue) API
* **Storage**: Cloudinary API
* **API Documentation**: Springdoc OpenAPI / Swagger UI

---

## Getting Started

### 1. Environment Configuration (`.env`)
Create a `.env` file in the root directory (this file is excluded from git) and provide the necessary configuration:

```env
# Database Settings
DB_HOST=localhost
DB_PORT=5432
DB_NAME=xpress
DB_USERNAME=postgres
DB_PASSWORD=your_postgres_password

# Brevo (email) API
BREVO_API_KEY=your_brevo_api_key_here
MAIL_URL=https://api.brevo.com/v3/smtp/email

# Application Identity & Security
APP_NAME=Xpress
APP_EMAIL=oneblockhq@gmail.com
ACCESS_TOKEN_EXP=3
REFRESH_TOKEN_EXP=168
SECRET_KEY=your_secure_base64_jwt_key_here

# Cloudinary Storage
CLOUDINARY_NAME=your_cloudinary_name_here
CLOUDINARY_KEY=your_cloudinary_key_here
CLOUDINARY_SECRET=your_cloudinary_secret_here
```

### 2. Database Setup
Create a PostgreSQL database named `xpress`. The schema migrations will execute automatically when the application starts.

### 3. Build & Run Tests
To compile the project and run all unit and integration tests:
```bash
./mvnw clean test
```

### 4. Running the Application
To run the Spring Boot server locally:
```bash
./mvnw spring-boot:run
```
Once started, the API will be accessible at `http://localhost:8080`.

### 5. API Documentation
Access the interactive Swagger UI documentation at:
`http://localhost:8080/swagger-ui/index.html`
