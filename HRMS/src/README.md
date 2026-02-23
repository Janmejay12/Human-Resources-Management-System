🏢 Human Resource Management System (HRMS) – Backend

Backend service for an enterprise-grade Human Resource Management System (HRMS) designed to manage:

Travel lifecycle management

Expense tracking and approvals

Document uploads and storage

Organizational hierarchy

Job portal and referrals

Game slot booking system

Built with Spring Boot 3, secured using JWT Authentication, and integrated with Cloudinary for scalable document storage.

📦 Technology Stack
Layer Technology
Backend Framework Spring Boot 3
Language Java 21
Database Microsoft SQL Server
ORM Spring Data JPA / Hibernate
Security Spring Security + JWT
File Storage Cloudinary
API Docs OpenAPI / Swagger
Build Tool Gradle
🔐 Authentication & Authorization

✔ JWT-based authentication
✔ Stateless session management
✔ Role-based access control

Supported Roles:

👨‍💼 HR

👨‍💻 Employee

Endpoint:

POST /api/auth/login
✈️ Travel Management
HR Capabilities

Create travel requests

Assign employees

Set per-day allowance

Cancel travel

Mark travel as completed

Employee Capabilities

Approve travel

Reject travel

Endpoints
GET /api/travels
GET /api/travels/{id}
GET /api/travels/my-travels
POST /api/travels
PUT /api/travels/{id}/status
DELETE /api/travels/{id}
📁 Travel Document Management

Cloud-based document storage using Cloudinary.

Supports:

Travel tickets

Identification documents

Permits

Supporting files

Endpoints:

POST /api/travels/{travelId}/documents
GET /api/travels/{travelId}/documents
GET /api/travels/{travelId}/documents/{documentId}
💰 Expense Management

Employees can submit expenses linked to travel.

HR can approve or reject expenses.

Features:

Travel-linked expenses

Employee ownership validation

Expense lifecycle tracking

Endpoints:

GET /api/travels/{travelId}/expenses
GET /api/travels/{travelId}/expenses/my
GET /api/travels/{travelId}/expenses/{expenseId}
POST /api/travels/{travelId}/expenses
PUT /api/travels/{travelId}/expenses/{expenseId}/status
🏢 Organization Chart

Supports hierarchical employee relationships.

Endpoint:

GET /api/employees/{id}/org-charts
💼 Job Portal & Referral System

Features:

Create jobs

Share jobs via email

Refer candidates

Upload resumes

Endpoints:

GET /api/jobs
POST /api/jobs
PUT /api/jobs/{id}
POST /api/jobs/{id}/share
POST /api/jobs/{id}/referals
🎮 Game Slot Booking System

Features:

Create games

Define slots

Book slots

Cancel bookings

Endpoints:

GET /api/games
PUT /api/games/{id}

GET /api/games/{id}/game-slots

POST /api/bookings/book-slots
PUT /api/bookings/cancel/{bookingId}

GET /api/bookings/my-bookings/today
👥 Employee Management

Admin can manage employees.

Endpoints:

POST /api/admin/register-employee
GET /api/admin/employees
☁️ Cloudinary Integration

Used for:

Travel documents

Expense receipts

Referral resumes

Provides:

Secure cloud storage

Automatic URL generation

Scalable storage

📚 API Documentation

Swagger UI:

http://localhost:8090/swagger-ui.html

OpenAPI Spec:

http://localhost:8090/v3/api-docs
⚙️ Installation & Setup
Prerequisites

Java 21+

Gradle

SQL Server

Cloudinary account

Clone Repository
git clone https://github.com/yourusername/hrms-backend.git
cd hrms-backend
Configure Database

Create database:

CREATE DATABASE HRMS_DB;

Update application.properties:

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=HRMS_DB;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

jwt.secret=your_secret
jwt.expiration=86400000

cloudinary.cloud_name=your_cloud
cloudinary.api_key=your_key
cloudinary.api_secret=your_secret
Run Application
./gradlew bootRun

Server starts at:

http://localhost:8090
📁 Project Structure
hrms-backend/
│
├── controllers/
├── services/
├── repositories/
├── entities/
├── dtos/
├── security/
├── mappers/
├── config/
└── application.properties
🛡 Security Features

JWT authentication

Role-based authorization

Stateless architecture

Secure endpoints

🚧 Current Limitations

No analytics dashboard

No audit logging

No email notification system

No reporting module

🚀 Future Enhancements

Email notifications

Expense analytics

Real-time updates

Admin dashboard

Reporting system

👨‍💻 Author

Human Resource Management System Backend
Built with Spring Boot, JWT, SQL Server, and Cloudinary