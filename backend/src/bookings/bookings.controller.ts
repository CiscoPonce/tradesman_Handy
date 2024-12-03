import { Controller, Get, Post, Body, Param, Put, UseGuards } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { BookingsService } from './bookings.service';
import { CreateBookingDto } from './dto/create-booking.dto';
import { Booking, BookingStatus } from './entities/booking.entity';

@ApiTags('bookings')
@Controller('bookings')
export class BookingsController {
  constructor(private readonly bookingsService: BookingsService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new booking' })
  @ApiResponse({ status: 201, description: 'Booking created successfully', type: Booking })
  async create(
    @Body() createBookingDto: CreateBookingDto,
  ): Promise<Booking> {
    return this.bookingsService.create(createBookingDto, createBookingDto.clientId);
  }

  @Get('tradesman/:tradesmanId')
  @ApiOperation({ summary: 'Get all bookings for a tradesman' })
  @ApiResponse({ status: 200, description: 'Returns all bookings', type: [Booking] })
  async findAllForTradesman(@Param('tradesmanId') tradesmanId: string): Promise<Booking[]> {
    return this.bookingsService.findAllForTradesman(tradesmanId);
  }

  @Put(':id/quote')
  @ApiOperation({ summary: 'Submit a quote for a booking' })
  @ApiResponse({ status: 200, description: 'Quote submitted successfully', type: Booking })
  async submitQuote(
    @Param('id') id: string,
    // TODO: Get from JWT token
    tradesmanId: string,
    @Body('price') price: number,
  ): Promise<Booking> {
    return this.bookingsService.updateStatus(id, tradesmanId, BookingStatus.QUOTED, price);
  }

  @Put(':id/accept')
  @ApiOperation({ summary: 'Accept a booking' })
  @ApiResponse({ status: 200, description: 'Booking accepted successfully', type: Booking })
  async acceptBooking(
    @Param('id') id: string,
    // TODO: Get from JWT token
    tradesmanId: string,
  ): Promise<Booking> {
    return this.bookingsService.updateStatus(id, tradesmanId, BookingStatus.ACCEPTED);
  }

  @Put(':id/reject')
  @ApiOperation({ summary: 'Reject a booking' })
  @ApiResponse({ status: 200, description: 'Booking rejected successfully', type: Booking })
  async rejectBooking(
    @Param('id') id: string,
    // TODO: Get from JWT token
    tradesmanId: string,
  ): Promise<Booking> {
    return this.bookingsService.updateStatus(id, tradesmanId, BookingStatus.REJECTED);
  }

  @Put(':id/schedule')
  @ApiOperation({ summary: 'Schedule a booking' })
  @ApiResponse({ status: 200, description: 'Booking scheduled successfully', type: Booking })
  async scheduleBooking(
    @Param('id') id: string,
    // TODO: Get from JWT token
    tradesmanId: string,
    @Body('scheduledDate') scheduledDate: Date,
  ): Promise<Booking> {
    return this.bookingsService.updateSchedule(id, tradesmanId, scheduledDate);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a booking by id' })
  @ApiResponse({ status: 200, description: 'Returns the booking', type: Booking })
  async findOne(@Param('id') id: string): Promise<Booking> {
    return this.bookingsService.findOne(id);
  }
}
