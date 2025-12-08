# RideShare Backend API

---

## Project Overview

This is a complete ride sharing backend system similar to Uber/Ola, built using Spring Boot and MongoDB. The application supports two types of users (passengers and drivers) with full authentication, ride request management, and role based access control.

## Technologies Implemented

- **Spring Boot 4.0.0** - Main application framework
- **MongoDB** - NoSQL database for data persistence
- **JWT (JSON Web Tokens)** - Secure authentication mechanism
- **Spring Security** - Authorization and authentication
- **Jakarta Validation** - Input validation on all endpoints
- **BCrypt** - Password hashing and encryption

## Core Features Implemented

All required features have been successfully implemented:

1. **User Registration and Login** - Secure signup with role selection and JWT based login
2. **Create Ride Request** - Passengers can request rides with pickup and drop locations
3. **View Pending Rides** - Drivers can see all available ride requests
4. **Accept Ride** - Drivers can accept specific ride requests
5. **Complete Ride** - Both users and drivers can mark rides as completed
6. **View Ride History** - Users can view all their past and current rides
7. **Global Exception Handling** - Proper error messages for all failure scenarios

## Project Structure

```
src/main/java/com/pushkar/ridebackend/
├── config/          - Security configuration (JWT, BCrypt, Spring Security)
├── controller/      - REST API endpoints (AuthController, RideController)
├── dto/            - Request/Response objects with validation
├── exception/      - Global exception handler and custom exceptions
├── model/          - Database entities (User, Ride)
├── repository/     - MongoDB data access layer
└── service/        - Business logic layer
```

## Database Design

### User Collection

- **id**: Unique identifier
- **username**: User's login name
- **password**: BCrypt encrypted password
- **role**: Either ROLE_USER (passenger) or ROLE_DRIVER

### Ride Collection

- **id**: Unique identifier
- **userId**: Reference to passenger
- **driverId**: Reference to driver (null until accepted)
- **pickupLocation**: Starting point
- **dropLocation**: Destination
- **status**: REQUESTED → ACCEPTED → COMPLETED
- **createdAt**: Timestamp

## How to Run the Application

### Prerequisites

- Java 17 or higher installed
- MongoDB running on localhost:27017
- Maven installed

### Steps to Run

1. **Start MongoDB**

```bash
# Make sure MongoDB is running on port 27017
mongod
```

2. **Build the Project**

```bash
cd RideBackend
mvn clean install
```

3. **Run the Application**

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8081**

4. **Build the project**

```bash
mvn clean install
```

5. **Run the application**

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081`

## API Endpoints Summary

| #   | Method | Endpoint                               | Role Required            | Description                         |
| --- | ------ | -------------------------------------- | ------------------------ | ----------------------------------- |
| 1   | POST   | `/api/auth/register`                   | Public                   | Register new user or driver account |
| 2   | POST   | `/api/auth/login`                      | Public                   | Login and receive JWT token         |
| 3   | POST   | `/api/v1/rides`                        | ROLE_USER                | Create new ride request             |
| 4   | GET    | `/api/v1/user/rides`                   | ROLE_USER                | View all rides for logged in user   |
| 5   | GET    | `/api/v1/driver/rides/requests`        | ROLE_DRIVER              | View all pending ride requests      |
| 6   | POST   | `/api/v1/driver/rides/{rideId}/accept` | ROLE_DRIVER              | Accept a specific ride request      |
| 7   | POST   | `/api/v1/rides/{rideId}/complete`      | ROLE_USER or ROLE_DRIVER | Mark ride as completed              |

---

## API Endpoints Documentation

The application exposes 7 REST API endpoints for managing rides and authentication.

### Authentication Endpoints (Public Access)

#### 1. Register User or Driver

**Endpoint:** `POST /api/auth/register`
**Description:** Creates a new user account (either passenger or driver)
**Authentication:** None required

**Request Body:**

```json
{
  "username": "alice",
  "password": "password123",
  "role": "ROLE_USER"
}
```

**Validation Rules:**

- Username: Required, minimum 3 characters, maximum 25 characters
- Password: Required, minimum 2 characters
- Role: Must be either "ROLE_USER" or "ROLE_DRIVER"

**Response:** Returns the created user object with encrypted password

---

#### 2. User Login

**Endpoint:** `POST /api/auth/login`
**Description:** Authenticates user and returns JWT token
**Authentication:** None required

**Request Body:**

```json
{
  "username": "alice",
  "password": "password123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZSIsInJvbGUiOiJST0xFX1VTRVIi..."
}
```

**Important:** Save this token and include it in all subsequent requests in the Authorization header:

```
Authorization: Bearer <your-token-here>
```

---

### Passenger Endpoints (ROLE_USER Required)

#### 3. Create Ride Request

**Endpoint:** `POST /api/v1/rides`
**Description:** Passenger creates a new ride request
**Authentication:** JWT token with ROLE_USER required

**Request Headers:**

```
Authorization: Bearer <user-token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar"
}
```

**Validation Rules:**

- pickupLocation: Required, cannot be blank
- dropLocation: Required, cannot be blank

**Response:** Returns created ride with status "REQUESTED"

---

#### 4. View My Rides

**Endpoint:** `GET /api/v1/user/rides`
**Description:** View all rides created by the logged in user
**Authentication:** JWT token with ROLE_USER required

**Request Headers:**

```
Authorization: Bearer <user-token>
```

**Response:** Returns array of all rides for this user

---

### Driver Endpoints (ROLE_DRIVER Required)

#### 5. View Pending Ride Requests

**Endpoint:** `GET /api/v1/driver/rides/requests`
**Description:** View all rides with status "REQUESTED" that need drivers
**Authentication:** JWT token with ROLE_DRIVER required

**Request Headers:**

```
Authorization: Bearer <driver-token>
```

**Response:** Returns array of all pending ride requests

---

#### 6. Accept Ride Request

**Endpoint:** `POST /api/v1/driver/rides/{rideId}/accept`
**Description:** Driver accepts a specific ride request
**Authentication:** JWT token with ROLE_DRIVER required

**Request Headers:**

```
Authorization: Bearer <driver-token>
```

**Path Parameters:**

- rideId: The ID of the ride to accept

**Business Rules:**

- Ride must exist
- Ride status must be "REQUESTED"
- Changes status to "ACCEPTED" and assigns driver

**Response:** Returns updated ride with status "ACCEPTED"

---

### Shared Endpoints (ROLE_USER or ROLE_DRIVER)

#### 7. Complete Ride

**Endpoint:** `POST /api/v1/rides/{rideId}/complete`
**Description:** Mark a ride as completed
**Authentication:** JWT token required (USER or DRIVER)

**Request Headers:**

```
Authorization: Bearer <user-or-driver-token>
```

**Path Parameters:**

- rideId: The ID of the ride to complete

**Business Rules:**

- Ride must exist
- Ride status must be "ACCEPTED"
- Only the passenger or assigned driver can complete
- Changes status to "COMPLETED"

**Response:** Returns updated ride with status "COMPLETED"

---

## Testing the Application

### Testing with cURL Commands

#### Step 1: Register a Passenger

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "pass123",
    "role": "ROLE_USER"
  }'
```

#### Step 2: Register a Driver

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "bob",
    "password": "pass123",
    "role": "ROLE_DRIVER"
  }'
```

#### Step 3: Login as Passenger

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "pass123"
  }'
```

**Important:** Copy the token from the response

#### Step 4: Create Ride Request

```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer <PASTE_USER_TOKEN_HERE>" \
  -H "Content-Type: application/json" \
  -d '{
    "pickupLocation": "MG Road",
    "dropLocation": "Whitefield"
  }'
```

**Note:** Copy the ride ID from the response

#### Step 5: Login as Driver

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "bob",
    "password": "pass123"
  }'
```

**Important:** Copy the driver token from the response

#### Step 6: View Pending Rides (as Driver)

```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
  -H "Authorization: Bearer <PASTE_DRIVER_TOKEN_HERE>"
```

#### Step 7: Accept Ride (as Driver)

```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/<RIDE_ID>/accept \
  -H "Authorization: Bearer <PASTE_DRIVER_TOKEN_HERE>"
```

#### Step 8: Complete Ride

```bash
curl -X POST http://localhost:8081/api/v1/rides/<RIDE_ID>/complete \
  -H "Authorization: Bearer <PASTE_TOKEN_HERE>"
```

#### Step 9: View My Rides (as User)

```bash
curl -X GET http://localhost:8081/api/v1/user/rides \
  -H "Authorization: Bearer <PASTE_USER_TOKEN_HERE>"
```

### Alternative Testing Method: Postman Collection

A complete Postman collection is provided in the file `RideSharing-API.postman_collection.json`. Import this file into Postman for easier testing with automatic token management.

---

## Security Implementation

### Authentication Flow

1. User registers with username, password, and role
2. Password is encrypted using BCrypt before storing
3. On login, credentials are validated and JWT token is generated
4. Token contains user information (username, role, expiration)
5. All protected endpoints require valid JWT token in Authorization header
6. Token is validated on every request

### Security Features

- **BCrypt Password Hashing** - Passwords stored securely with one way encryption
- **JWT Token Based Auth** - Stateless authentication without server sessions
- **Role Based Access Control** - Endpoints restricted by user roles
- **Token Expiration** - Tokens expire after 24 hours
- **Input Validation** - All inputs validated before processing

---

## Error Handling

All errors return a consistent JSON format:

```json
{
  "error": "ERROR_TYPE",
  "message": "Human readable error message",
  "timestamp": "2025-12-07T12:00:00Z"
}
```

### Common Error Types

| Error Code       | HTTP Status | Description                                         |
| ---------------- | ----------- | --------------------------------------------------- |
| VALIDATION_ERROR | 400         | Input validation failed (missing or invalid fields) |
| NOT_FOUND        | 404         | Requested resource does not exist                   |
| BAD_REQUEST      | 400         | Invalid request (wrong status, unauthorized action) |
| UNAUTHORIZED     | 401         | Invalid credentials or missing token                |
| INTERNAL_ERROR   | 500         | Server error                                        |

### Example Error Scenarios

**Missing Required Field:**

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Pickup location is required",
  "timestamp": "2025-12-07T10:30:00Z"
}
```

**Ride Not Found:**

```json
{
  "error": "NOT_FOUND",
  "message": "Ride not found",
  "timestamp": "2025-12-07T10:30:00Z"
}
```

**Invalid Ride Status:**

```json
{
  "error": "BAD_REQUEST",
  "message": "Ride is not in REQUESTED status",
  "timestamp": "2025-12-07T10:30:00Z"
}
```

---

## Business Rules and Validation

### User Registration

- Username must be 3-25 characters
- Password must be at least 2 characters
- Role must be either ROLE_USER or ROLE_DRIVER
- Username must be unique

### Ride Creation

- Only users with ROLE_USER can create rides
- Pickup and drop locations are required
- Initial status is always REQUESTED

### Ride Acceptance

- Only drivers (ROLE_DRIVER) can accept rides
- Can only accept rides with status REQUESTED
- Once accepted, status changes to ACCEPTED and driver is assigned

### Ride Completion

- Only rides with status ACCEPTED can be completed
- Can be completed by either the passenger or assigned driver
- Once completed, status changes to COMPLETED

---

## Project Deliverables

This submission includes:

1. **Complete Source Code** - All Java files with proper structure
2. **README.md** - This comprehensive documentation
3. **Postman Collection** - Ready to import test collection
4. **Build Configuration** - pom.xml with all dependencies
5. **Application Properties** - MongoDB and JWT configuration

---

## Technical Implementation Details

### Architecture Pattern

The application follows the standard three layer architecture:

- **Controller Layer** - Handles HTTP requests and responses
- **Service Layer** - Contains business logic and validation
- **Repository Layer** - Handles database operations

### Key Components

**Controllers:**

- AuthController - Handles registration and login
- RideController - Manages all ride related operations

**Services:**

- AuthService - Authentication logic
- UserService - User management
- RideService - Ride business logic
- JwtService - Token generation and validation

**Repositories:**

- UserRepository - User database operations
- RideRepository - Ride database operations

**DTOs (Data Transfer Objects):**

- RegisterRequest - User registration data with validation
- LoginRequest - Login credentials with validation
- RideRequest - Ride creation data with validation
- AuthResponse - JWT token response

**Exception Handling:**

- GlobalExceptionHandler - Centralized error handling
- NotFoundException - Custom exception for missing resources
- BadRequestException - Custom exception for invalid requests
