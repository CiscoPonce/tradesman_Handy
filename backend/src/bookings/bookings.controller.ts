import { Controller, Get, Post, Body, Param, Put, Delete, UseGuards, Headers } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { BookingsService } from './bookings.service';
import { CreateBookingDto } from './dto/create-booking.dto';
import { Booking, BookingStatus } from './entities/booking.entity';
import { NotFoundException, ForbiddenException } from '@nestjs/common';
import { UpdateQuoteDto } from './dto/update-quote.dto';
import { ClientAuthGuard } from '../auth/client-auth.guard';

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

  @Get()
  @ApiOperation({ summary: 'Get all bookings' })
  @ApiResponse({ status: 200, description: 'Return all bookings', type: [Booking] })
  async findAll(): Promise<Booking[]> {
    return this.bookingsService.findAll();
  }

  @Get('tradesman/:tradesmanId')
  @ApiOperation({ summary: 'Get all bookings for a tradesman' })
  @ApiResponse({ status: 200, description: 'Returns all bookings', type: [Booking] })
  async findAllForTradesman(@Param('tradesmanId') tradesmanId: string): Promise<Booking[]> {
    return this.bookingsService.findAllForTradesman(tradesmanId);
  }

  @Get('client/:clientId')
  @ApiOperation({ summary: 'Get all bookings for a client' })
  @ApiResponse({ status: 200, description: 'Return all bookings for a client', type: [Booking] })
  async findAllForClient(@Param('clientId') clientId: string): Promise<Booking[]> {
    return this.bookingsService.findAllForClient(clientId);
  }

  @Put(':id/quote')
  @UseGuards(ClientAuthGuard)
  @ApiOperation({ summary: 'Submit a quote for a booking' })
  @ApiResponse({ status: 200, description: 'Quote submitted successfully', type: Booking })
  async updateQuote(
    @Param('id') id: string,
    @Body() updateQuoteDto: UpdateQuoteDto,
  ): Promise<Booking> {
    const updatedBooking = await this.bookingsService.updateQuote(
      id,
      updateQuoteDto.tradesmanId,
      updateQuoteDto.quotedPrice,
      new Date(updateQuoteDto.scheduledDate),
    );
    
    // Fetch the fresh booking to ensure we have the latest data
    return this.bookingsService.findOne(id);
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

  @Delete('all')
  @ApiOperation({ summary: 'Delete all bookings' })
  @ApiResponse({ status: 200, description: 'All bookings deleted successfully' })
  async deleteAll(): Promise<void> {
    return this.bookingsService.deleteAll();
  }

  @Post('reset-table')
  async resetBookingsTable() {
    return this.bookingsService.resetBookingsTable();
  }
}
