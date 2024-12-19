# Tradesman Handy - Service Booking Management System

A comprehensive mobile application for connecting tradesmen with customers, featuring a modern Android app frontend and a robust NestJS backend.

## Project Overview

Tradesman Handy is a comprehensive mobile platform designed to streamline the connection between skilled tradespeople and clients. The project consists of two main components: a mobile app for tradesmen built with Android/Kotlin, and a robust backend service powered by NestJS.

### Mobile App (Android/Kotlin)
The Android application is built using modern Android development practices and Material 3 Design principles. It features:
- Clean Architecture with MVVM pattern
- Jetpack Compose for modern UI development
- Kotlin Coroutines for asynchronous operations
- Dagger Hilt for dependency injection
- Room Database for local data persistence
- Material 3 components for a polished, professional look

Key features include:
- Booking management system for tradesmen
- Real-time job notifications
- Profile management
- Schedule organization
- Client communication tools

### Backend (NestJS/TypeScript)
The backend service is built with NestJS, providing a scalable and maintainable API. It includes:
- RESTful API endpoints for all app features
- PostgreSQL database with TypeORM
- JWT-based authentication
- Role-based access control
- Automated migrations for database schema
- Swagger API documentation

The backend handles:
- User authentication and authorization
- Booking management and scheduling
- Client-tradesman matching
- Notification dispatch
- Data persistence and retrieval

## Project Structure

```
tradesman_Handy/
├── android/          # Android mobile app (Kotlin)
│   ├── app/         # Main application module
│   └── gradle/      # Gradle configuration
└── backend/         # NestJS backend server
    ├── src/         # Source code
    └── test/        # Test files
```

## Technology Stack

### Mobile App (Frontend)
- Kotlin for Android
- Jetpack Compose for modern UI
- Retrofit for API communication
- Kotlin Coroutines for async operations
- Dagger Hilt for dependency injection
- Moshi for JSON parsing
- JUnit and Mockito for testing

### Backend
- NestJS (Node.js framework)
- PostgreSQL database
- TypeORM for database operations
- JWT authentication
- Swagger for API documentation

## Current Features
- User authentication for tradesmen
- Booking management system
  - View pending bookings
  - Accept/reject bookings
  - View booking details
  - Track booking status
- Dashboard with booking statistics
- Calendar view for scheduled bookings
- Modern Material 3 UI design
- Dark/Light theme support
- Responsive layout for different screen sizes

## Upcoming Features
- Chat functionality between customers and tradesmen
- Rating and review system
- Push notifications for booking updates
- Customer mobile app
- Payment integration
- Service provider portfolios
- Advanced search and filtering

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17 or later
- Android SDK 34
- Node.js 18+ (for backend)
- PostgreSQL 14+

### Android App Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/tradesman_Handy.git
   ```

2. Open the android folder in Android Studio

3. Configure your local.properties file with:
   ```properties
   sdk.dir=YOUR_ANDROID_SDK_PATH
   ```

4. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```

5. Run the app on an emulator or physical device

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Configure your .env file with:
   ```env
   DATABASE_URL=your_postgres_url
   JWT_SECRET=your_jwt_secret
   ```

4. Start the development server:
   ```bash
   npm run start:dev
   ```

## Testing
- Frontend: Unit tests using JUnit and Mockito
- Backend: Integration and e2e tests using Jest

## Contributing
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## Current Status (as of December 19, 2024)
- Android app: Beta testing phase
  - Core booking management features implemented
  - UI/UX improvements ongoing
  - Unit tests being added
- Backend: In development
  - Basic API endpoints implemented
  - Database schema established
  - Authentication system in place


