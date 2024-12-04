# Tradesman Handy Backend

A robust NestJS backend service for the Tradesman Handy mobile application, providing API endpoints for managing tradesman bookings and services.

## Features

- **User Management**
  - User registration and authentication
  - Role-based access (Tradesman/Client)
  - Profile management

- **Booking System**
  - Create and manage bookings
  - Quote submission and management
  - Booking status tracking
  - Schedule management

- **API Documentation**
  - Swagger UI integration
  - Comprehensive API documentation
  - Request/Response examples

## Tech Stack

- **Framework**: NestJS
- **Language**: TypeScript
- **Database**: PostgreSQL (Neon)
- **ORM**: TypeORM
- **Documentation**: Swagger/OpenAPI
- **Authentication**: JWT (prepared)
- **Validation**: class-validator
- **API Testing**: Jest

## Prerequisites

- Node.js (v14 or higher)
- npm (v6 or higher)
- PostgreSQL database

## Environment Variables

Create a `.env` file in the root directory:

```env
DATABASE_URL=your_postgres_connection_string
NODE_ENV=development
PORT=3000
```

## Installation

1. Install dependencies:
   ```bash
   npm install
   ```

2. Build the application:
   ```bash
   npm run build
   ```

3. Start the server:
   ```bash
   # Development
   npm run start:dev

   # Production
   npm run start:prod
   ```

## API Documentation

Once the server is running, access the Swagger documentation at:
```
http://localhost:3000/api
```

## API Endpoints

### Users
- `POST /users` - Create a new user
- `GET /users/:id` - Get user by ID

### Bookings
- `POST /bookings` - Create a new booking
- `GET /bookings/tradesman/:tradesmanId` - Get tradesman's bookings
- `PUT /bookings/:id/quote` - Submit a quote
- `PUT /bookings/:id/accept` - Accept a booking
- `PUT /bookings/:id/reject` - Reject a booking
- `PUT /bookings/:id/schedule` - Schedule a booking

## Security

- CORS enabled with configurable origins
- Request validation using class-validator
- Prepared for JWT authentication
- Input sanitization
- SSL/TLS support

## Testing

```bash
# Unit tests
npm run test

# e2e tests
npm run test:e2e

# Test coverage
npm run test:cov
```

## Production Deployment

The backend is deployed on Render.com with the following configuration:

- **Build Command**: `chmod +x build.sh && ./build.sh`
- **Start Command**: `npm run start:prod`
- **Environment Variables**: Configured in Render dashboard
- **Auto-Deploy**: Enabled for the main branch

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
