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

    booking.scheduledDate = scheduledDate ? new Date(scheduledDate) : null;
    return this.bookingsRepository.save(booking);
  }

  async updateQuote(
    id: string,
    tradesmanId: string,
    quotedPrice: number,
    scheduledDate: Date,
  ): Promise<Booking> {
    // Check table structure
    const tableStructure = await this.bookingsRepository.query(`
      SELECT 
        column_name,
        data_type,
        is_nullable,
        column_default,
        character_maximum_length
      FROM information_schema.columns 
      WHERE table_name = 'bookings'
      ORDER BY ordinal_position;
    `);
    console.log('Table structure:', JSON.stringify(tableStructure, null, 2));

    // Check table constraints
    const tableConstraints = await this.bookingsRepository.query(`
      SELECT 
        tc.constraint_name,
        tc.constraint_type,
        kcu.column_name,
        ccu.table_name AS foreign_table_name,
        ccu.column_name AS foreign_column_name
      FROM information_schema.table_constraints tc
      JOIN information_schema.key_column_usage kcu
        ON tc.constraint_name = kcu.constraint_name
      LEFT JOIN information_schema.constraint_column_usage ccu
        ON tc.constraint_name = ccu.constraint_name
      WHERE tc.table_name = 'bookings';
    `);
    console.log('Table constraints:', JSON.stringify(tableConstraints, null, 2));

    // Check current booking data
    const currentBooking = await this.bookingsRepository.query(`
      SELECT *
      FROM bookings
      WHERE id = $1;
    `, [id]);
    console.log('Current booking:', JSON.stringify(currentBooking, null, 2));

    // Try to update with explicit type casting and RETURNING clause
    const updateResult = await this.bookingsRepository.query(`
      UPDATE bookings 
      SET 
        status = CAST($1 AS text),
        quoted_price = CAST($2 AS numeric(10,2)),
        scheduled_date = CAST($3 AS timestamptz),
        updated_at = CURRENT_TIMESTAMP
      WHERE id = CAST($4 AS uuid)
      RETURNING *;
    `, [BookingStatus.QUOTED, quotedPrice, scheduledDate.toISOString(), id]);
    console.log('Update result:', JSON.stringify(updateResult, null, 2));

    // Return the updated booking
    const updatedBooking = await this.bookingsRepository.findOne({
      where: { id },
      relations: ['client', 'tradesman'],
    });

    if (!updatedBooking) {
      throw new NotFoundException('Updated booking not found');
    }

    return updatedBooking;
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
    await this.bookingsRepository.query('DELETE FROM bookings');
  }
}
