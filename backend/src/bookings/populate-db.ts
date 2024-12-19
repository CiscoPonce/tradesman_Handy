import { NestFactory } from '@nestjs/core';
import { AppModule } from '../app.module';
import { BookingsService } from './bookings.service';
import { generatePendingBookings, generateAcceptedBookings, generateCompletedBookings } from './test-data';
import { BookingStatus } from './entities/booking.entity';

async function bootstrap() {
  const app = await NestFactory.createApplicationContext(AppModule);
  const bookingsService = app.get(BookingsService);

  try {
    // Create pending bookings
    console.log('Creating pending bookings...');
    const pendingBookings = generatePendingBookings();
    for (const booking of pendingBookings) {
      await bookingsService.create(booking, booking.clientId);
    }

    // Create accepted bookings
    console.log('Creating accepted bookings...');
    const acceptedBookings = generateAcceptedBookings();
    for (const booking of acceptedBookings) {
      const createdBooking = await bookingsService.create(booking, booking.clientId);
      await bookingsService.updateStatus(
        createdBooking.id,
        booking.tradesmanId,
        BookingStatus.ACCEPTED,
        Math.floor(Math.random() * 500) + 100
      );
    }

    // Create completed bookings
    console.log('Creating completed bookings...');
    const completedBookings = generateCompletedBookings();
    for (const booking of completedBookings) {
      const createdBooking = await bookingsService.create(booking, booking.clientId);
      await bookingsService.updateStatus(
        createdBooking.id,
        booking.tradesmanId,
        BookingStatus.COMPLETED,
        Math.floor(Math.random() * 500) + 100
      );
    }

    console.log('Database populated successfully!');
  } catch (error) {
    console.error('Error populating database:', error);
  } finally {
    await app.close();
  }
}

bootstrap();
