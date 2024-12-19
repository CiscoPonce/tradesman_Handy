import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
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
    const tradesman = await this.usersService.findById(createBookingDto.tradesmanId);
    if (!tradesman.isTradesmen) {
      throw new ForbiddenException('Selected user is not a tradesman');
    }

    const booking = this.bookingsRepository.create({
      ...createBookingDto,
      clientId,
      status: BookingStatus.PENDING,
    });

    return this.bookingsRepository.save(booking);
  }

  async findAll(): Promise<Booking[]> {
    // Check if table exists and get table info
    try {
      const tables = await this.bookingsRepository.query(`
        SELECT table_name, table_schema 
        FROM information_schema.tables 
        WHERE table_schema = 'public'
        ORDER BY table_name;
      `);
      console.log('Tables in database:', tables);

      if (tables.some(t => t.table_name === 'bookings')) {
        const columns = await this.bookingsRepository.query(`
          SELECT column_name, data_type, is_nullable
          FROM information_schema.columns
          WHERE table_name = 'bookings'
          ORDER BY ordinal_position;
        `);
        console.log('Columns in bookings table:', columns);
      }

      return this.bookingsRepository.find({
        relations: ['client', 'tradesman'],
      });
    } catch (error) {
      console.error('Database error:', error);
      throw error;
    }
  }

  async findAllForTradesman(tradesmanId: string): Promise<Booking[]> {
    return this.bookingsRepository.find({
      where: { tradesmanId },
      relations: ['client'],
      order: {
        scheduledDate: 'ASC',
        createdAt: 'DESC',
      },
    });
  }

  async findAllForClient(clientId: string): Promise<Booking[]> {
    return this.bookingsRepository.find({
      where: { clientId },
      relations: ['client', 'tradesman'],
    });
  }

  async updateStatus(
    id: string,
    tradesmanId: string,
    status: BookingStatus,
    quotedPrice?: number,
  ): Promise<Booking> {
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

    return this.bookingsRepository.save(booking);
  }

  async updateSchedule(
    id: string,
    tradesmanId: string,
    scheduledDate: Date,
  ): Promise<Booking> {
    const booking = await this.bookingsRepository.findOne({
      where: { id, tradesmanId },
      relations: ['client', 'tradesman'],
    });

    if (!booking) {
      throw new NotFoundException('Booking not found');
    }

    booking.scheduledDate = scheduledDate;
    return this.bookingsRepository.save(booking);
  }

  async updateQuote(
    id: string,
    tradesmanId: string,
    quotedPrice: number,
    scheduledDate: Date,
  ): Promise<Booking> {
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

    return this.bookingsRepository.save(booking);
  }

  async findOne(id: string): Promise<Booking> {
    const booking = await this.bookingsRepository.findOne({
      where: { id },
      relations: ['client', 'tradesman'],
    });

    if (!booking) {
      throw new NotFoundException('Booking not found');
    }

    return booking;
  }

  async deleteAll(): Promise<void> {
    await this.bookingsRepository.clear();
  }
}
