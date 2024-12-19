import { Injectable, NotFoundException, ForbiddenException, InternalServerErrorException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Booking, BookingStatus } from './entities/booking.entity';
import { CreateBookingDto } from './dto/create-booking.dto';
import { UsersService } from '../users/users.service';

@Injectable()
export class BookingsService {
  constructor(
    @InjectRepository(Booking)
    private bookingsRepository: Repository<Booking>,
    private usersService: UsersService,
  ) {}

  async create(createBookingDto: CreateBookingDto, clientId: string): Promise<Booking> {
    try {
      const tradesman = await this.usersService.findById(createBookingDto.tradesmanId);
      if (!tradesman.isTradesmen) {
        throw new ForbiddenException('Selected user is not a tradesman');
      }

      const booking = this.bookingsRepository.create({
        ...createBookingDto,
        clientId,
        status: BookingStatus.PENDING,
      });

      return await this.bookingsRepository.save(booking);
    } catch (error) {
      console.error('Error creating booking:', error);
      throw new InternalServerErrorException('Failed to create booking');
    }
  }

  async findAll(): Promise<Booking[]> {
    try {
      // First check if the table exists
      const tables = await this.bookingsRepository.query(`
        SELECT EXISTS (
          SELECT FROM information_schema.tables 
          WHERE table_schema = 'public' 
          AND table_name = 'bookings'
        );
      `);
      
      if (!tables[0].exists) {
        console.error('Bookings table does not exist');
        throw new InternalServerErrorException('Bookings table not found');
      }

      // Then get all bookings
      const bookings = await this.bookingsRepository.find({
        relations: ['client', 'tradesman'],
      });

      return bookings;
    } catch (error) {
      console.error('Error fetching all bookings:', error);
      throw new InternalServerErrorException('Failed to fetch bookings');
    }
  }

  async findAllForTradesman(tradesmanId: string): Promise<Booking[]> {
    try {
      return await this.bookingsRepository.find({
        where: { tradesmanId },
        relations: ['client'],
        order: {
          scheduledDate: 'ASC',
          createdAt: 'DESC',
        },
      });
    } catch (error) {
      console.error(`Error fetching bookings for tradesman ${tradesmanId}:`, error);
      throw new InternalServerErrorException('Failed to fetch tradesman bookings');
    }
  }

  async findAllForClient(clientId: string): Promise<Booking[]> {
    try {
      return await this.bookingsRepository.find({
        where: { clientId },
        relations: ['client', 'tradesman'],
      });
    } catch (error) {
      console.error(`Error fetching bookings for client ${clientId}:`, error);
      throw new InternalServerErrorException('Failed to fetch client bookings');
    }
  }

  async updateStatus(
    id: string,
    tradesmanId: string,
    status: BookingStatus,
    quotedPrice?: number,
  ): Promise<Booking> {
    try {
      const booking = await this.bookingsRepository.findOne({
        where: { id, tradesmanId },
        relations: ['client', 'tradesman'],
      });

      if (!booking) {
        throw new NotFoundException('Booking not found');
      }

      booking.status = status;
      if (quotedPrice !== undefined) {
        booking.quotedPrice = quotedPrice;
      }

      return await this.bookingsRepository.save(booking);
    } catch (error) {
      if (error instanceof NotFoundException) throw error;
      console.error(`Error updating status for booking ${id}:`, error);
      throw new InternalServerErrorException('Failed to update booking status');
    }
  }

  async updateSchedule(
    id: string,
    tradesmanId: string,
    scheduledDate: Date,
  ): Promise<Booking> {
    try {
      const booking = await this.bookingsRepository.findOne({
        where: { id, tradesmanId },
        relations: ['client', 'tradesman'],
      });

      if (!booking) {
        throw new NotFoundException('Booking not found');
      }

      booking.scheduledDate = scheduledDate;
      return await this.bookingsRepository.save(booking);
    } catch (error) {
      if (error instanceof NotFoundException) throw error;
      console.error(`Error updating schedule for booking ${id}:`, error);
      throw new InternalServerErrorException('Failed to update booking schedule');
    }
  }

  async updateQuote(
    id: string,
    tradesmanId: string,
    quotedPrice: number,
    scheduledDate: Date,
  ): Promise<Booking> {
    try {
      const booking = await this.bookingsRepository.findOne({
        where: { id, tradesmanId },
        relations: ['client', 'tradesman'],
      });

      if (!booking) {
        throw new NotFoundException('Booking not found');
      }

      booking.quotedPrice = quotedPrice;
      booking.scheduledDate = scheduledDate;
      booking.status = BookingStatus.QUOTED;

      return await this.bookingsRepository.save(booking);
    } catch (error) {
      if (error instanceof NotFoundException) throw error;
      console.error(`Error updating quote for booking ${id}:`, error);
      throw new InternalServerErrorException('Failed to update booking quote');
    }
  }

  async findOne(id: string): Promise<Booking> {
    try {
      const booking = await this.bookingsRepository.findOne({
        where: { id },
        relations: ['client', 'tradesman'],
      });

      if (!booking) {
        throw new NotFoundException('Booking not found');
      }

      return booking;
    } catch (error) {
      if (error instanceof NotFoundException) throw error;
      console.error(`Error finding booking ${id}:`, error);
      throw new InternalServerErrorException('Failed to fetch booking');
    }
  }

  async deleteAll(): Promise<void> {
    try {
      await this.bookingsRepository.clear();
    } catch (error) {
      console.error('Error deleting all bookings:', error);
      throw new InternalServerErrorException('Failed to delete all bookings');
    }
  }
}
