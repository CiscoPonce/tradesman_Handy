import { IsString, IsEnum, IsOptional, IsNumber, IsUUID, IsDateString } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';
import { BookingSource } from '../entities/booking.entity';

export class CreateBookingDto {
  @ApiProperty()
  @IsString()
  title: string;

  @ApiProperty()
  @IsString()
  description: string;

  @ApiProperty({ enum: BookingSource })
  @IsEnum(BookingSource)
  source: BookingSource;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsNumber()
  quotedPrice?: number;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsDateString()
  scheduledDate?: string;

  @ApiProperty()
  @IsUUID()
  tradesmanId: string;

  @ApiProperty()
  @IsUUID()
  clientId: string;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  location?: string;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  housingAssociationRef?: string;
}
