📚 Book Rental Management System
A comprehensive Spring Boot REST API for managing book rentals with clean DTO architecture, Jakarta validation, and extensive testing.
✨ Features

🔐 Clean DTO Architecture - Separate Input/Output DTOs with validation
📚 Book Management - CRUD operations with search functionality
🏠 Rental Management - Create rentals, track books, handle returns
✅ Jakarta Validation - Proper input validation with custom error messages
🧪 Comprehensive Testing - 22 JUnit test cases with 100% pass rate
📖 API Documentation - Swagger UI documentation
🚀 Production Ready - Clean code, proper error handling, CORS support

🚀 Quick Start
Prerequisites

☕ Java 17+
📦 Maven 3.6+
🗄️ MySQL Database

1. Clone & Setup
git clone https://github.com/sapunethmini/Book-Rental-Management-System-Backend.git

2. Database Configuration
Option A: MySQL Database
Update src/main/resources/application.yml:
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/book_rental_db
    username: root
    password: your_password

Option B: H2 Database (Demo)
No configuration needed - just run with H2 profile:
mvn spring-boot:run -Dspring.profiles.active=h2

3. Run the Application
# Build the project
mvn clean install

# Run with MySQL
mvn spring-boot:run

# Or run with H2 for demo
mvn spring-boot:run -Dspring.profiles.active=h2

4. Access the Application

API Base URL: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html

📋 API Endpoints Overview
📚 Books API



Method
Endpoint
Description



POST
/api/books
Create new book


GET
/api/books
Get all books


GET
/api/books/available
Get available books


GET
/api/books/{id}
Get book by ID


PUT
/api/books/{id}
Update book


DELETE
/api/books/{id}
Delete book


🏠 Rentals API



Method
Endpoint
Description



POST
/api/rentals
Create new rental


GET
/api/rentals
Get all rentals


GET
/api/rentals/{id}
Get rental by ID


PUT
/api/rentals/{id}
Update rental


PUT
/api/rentals/{id}/return
Return books


🗂️ DTO Architecture
Book DTOs
CommonBookDTO (Base)
├── InputBookDTO (for create/update)
└── OutputBookDTO (for retrieval)

Rental DTOs
CommonRentalDTO (Base)
├── InputRentalDTO (for create/update)  
└── OutputRentalDTO (for retrieval)

📝 Example Usage
Create a Book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java Programming",
    "author": "John Doe",
    "genre": "Programming",
    "available": true
  }'

Create a Rental
curl -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d '{
    "userDetails": "John Smith - john@email.com",
    "rentalDate": "2024-01-01",
    "returnDate": "2024-01-15",
    "bookIds": [1, 2, 3]
  }'

🧪 Testing
# Run all tests
mvn test

# Run specific test suites
mvn test -Dtest=BookServiceTestFile
mvn test -Dtest=RentalServiceTestFile

# Generate test report
mvn surefire-report:report

Test Results: ✅ 22 tests, 0 failures, 0 errors
📦 Project Structure
src/
├── main/java/org/newnop/
│   ├── controller/          # REST Controllers
│   ├── dto/
│   │   ├── book/           # Book DTOs
│   │   └── rental/         # Rental DTOs
│   ├── entity/             # JPA Entities
│   ├── mapper/             # DTO-Entity Mappers
│   ├── repository/         # Data Repositories
│   └── service/            # Business Logic
└── test/java/org/newnop/   # Test Cases

📬 Postman Collection
Import Book_Rental_API.postman_collection.json into Postman for:

✅ Pre-configured requests
🔧 Environment variables
📋 Sample data
🧪 Complete test scenarios

🛠️ Technology Stack



Category
Technology



Framework
Spring Boot 3.3.3


Database
MySQL + Spring Data JPA


Validation
Jakarta Validation


Testing
JUnit 5 + Mockito


Documentation
SpringDoc OpenAPI 3


Build
Maven


Java
17+


🔧 Configuration
Database Setup
MySQL
CREATE DATABASE book_rental_db;
USE book_rental_db;
-- Tables are auto-created by JPA

H2 (Demo)



Application Properties
Key configurations in application.yml:

Database connection
JPA settings
Server port (8080)
CORS configuration

📊 Database Design
The database schema for the Book Rental Management System is designed to efficiently manage books and rentals. The Entity-Relationship (ER) diagram provides a clear overview of the database structure, including tables, relationships, and constraints.

📄 ER Diagram: View the detailed ER diagram here.

Key entities:

Books: Stores book details (title, author, genre, availability).
Rentals: Tracks rental information (user details, rental/return dates, associated books).
Relationships: Many-to-many relationship between Books and Rentals, managed via a join table.

The ER diagram illustrates the schema, including primary keys, foreign keys, and relationships, ensuring clarity for developers and database administrators.
📊 Business Logic
Book Management

✅ Create, read, update, delete operations
🔍 Search by title, author, or genre
📋 Filter available books for rental

Rental Management

📝 Create rentals with multiple books
🔒 Automatic book availability management
📅 Track rental and return dates
↩️ Return books and update availability

Validation Rules

📖 Books: Title and author required
🏠 Rentals: User details, dates, and book IDs required
🚫 Business Rules: Can't rent unavailable books

🔧 Database Connection Issue?
If you're getting database connection errors, use H2 for quick testing:
# Add H2 dependency to pom.xml (if not present)
mvn spring-boot:run -Dspring.profiles.active=h2

The H2 profile automatically configures an in-memory database.
🤝 Contributing

Fork the repository
Create your feature branch (git checkout -b feature/amazing-feature)
Commit your changes (git commit -m 'Add amazing feature')
Push to the branch (git push origin feature/amazing-feature)
Open a Pull Request

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
📞 Support
For questions or support:

🐛 Create an issue for bugs
💡 Submit feature requests via issues


Happy Coding! 🚀📚
