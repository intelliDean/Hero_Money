-- V1__db_setup.sql

-- 1. Create address table
CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    house_number VARCHAR(255),
    street_name VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255)
);

-- 2. Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address_id BIGINT UNIQUE REFERENCES address(id) ON DELETE SET NULL,
    user_image VARCHAR(255),
    phone_number VARCHAR(255),
    registered_at TIMESTAMP NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE
);

-- 3. Create user_roles table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);

-- 4. Create xpress_tokens table
CREATE TABLE xpress_tokens (
    id BIGSERIAL PRIMARY KEY,
    access_token VARCHAR(2048) NOT NULL,
    refresh_token VARCHAR(2048) NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- 5. Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    age INT NOT NULL,
    marital_status VARCHAR(255),
    job_status VARCHAR(255),
    salary NUMERIC(19, 2),
    company_name VARCHAR(255),
    gender VARCHAR(255),
    form_of_identity VARCHAR(255),
    complete BOOLEAN NOT NULL DEFAULT FALSE
);

-- 6. Create loan_officer table
CREATE TABLE loan_officer (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    employee_id VARCHAR(255)
);

-- 7. Create loan_documents table
CREATE TABLE loan_documents (
    id BIGSERIAL PRIMARY KEY,
    pay_slip VARCHAR(255),
    bank_statement VARCHAR(255)
);

-- 8. Create loans table
CREATE TABLE loans (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    application_date TIMESTAMP NOT NULL,
    loan_purpose VARCHAR(255),
    loan_amount NUMERIC(19, 2),
    repayment_term INT NOT NULL,
    interest_rate NUMERIC(19, 2),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    loan_status VARCHAR(255),
    disbursement_date TIMESTAMP,
    payment_frequency VARCHAR(255),
    repayment_amount NUMERIC(19, 2),
    loan_documents_id BIGINT UNIQUE REFERENCES loan_documents(id) ON DELETE SET NULL
);

-- 9. Create loan_agreement table
CREATE TABLE loan_agreement (
    id BIGSERIAL PRIMARY KEY,
    loan_id BIGINT UNIQUE REFERENCES loans(id) ON DELETE CASCADE,
    loan_officer_id BIGINT NOT NULL REFERENCES loan_officer(id) ON DELETE CASCADE,
    generated_at TIMESTAMP NOT NULL,
    agreed BOOLEAN NOT NULL DEFAULT FALSE
);

-- 10. Create init_token table
CREATE TABLE init_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255),
    email VARCHAR(255),
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    generated_at TIMESTAMP NOT NULL,
    expire_at TIMESTAMP NOT NULL,
    expired BOOLEAN NOT NULL DEFAULT FALSE
);
