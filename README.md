# Hero Money

This is a loan application built with Spring Boot, providing a platform for users to apply for loans and loan officers
to review and approve/reject loan applications.

### Features
* User Registration: Users can sign up for an account on the platform
* User Authentication: Users can log in to the platform using their credentials
* User Profile: Users can edit their profile information, including personal details and contact information.
* Loan Application: Users can submit loan applications through the platform, providing necessary information such as
  loan amount, purpose, and documentation.
* Loan Officer Access: Loan officers have separate login credentials and can review loan applications submitted by
  users.
* Loan Application Review: Loan officers can review and evaluate loan applications, considering factors such as credit
  history and documentation provided by the user.
* Loan Approval/Rejection: Loan officers can approve or reject loan applications based on their assessment.
* Loan Agreement Generation: For approved loan applications, loan officers can generate loan agreements for the
  borrowers to review and sign.

### Installation
* Clone the repository:
  git clone https://github.com/intelliDean/Hero_Money.git
* Configure the database settings, configure sendinblue/brevo with api key and mail url, configure cloudinary, input a
  string for the secret key, set the value of the expiry time for the access token and refresh token in the "
  application.yml" file.
* Build and run the application:
* Access the application at http://localhost:8181

### Technologies Used
* Java
* Spring Boot
* Spring Security
* PostgreSQL
* Brevo API
* Cloudinary API
* Thymeleaf

### Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a
pull request.
