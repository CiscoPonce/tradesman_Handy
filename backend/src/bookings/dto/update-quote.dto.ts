import { IsNumber, IsOptional, IsString, Min, IsUUID, IsDateString } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class UpdateQuoteDto {
  @ApiProperty({
    description: 'The ID of the tradesman providing the quote',
    example: '123e4567-e89b-12d3-a456-426614174000',
    required: true,
  })
  @IsUUID()
  tradesmanId: string;

  @ApiProperty({
    description: 'The quoted price for the booking',
    example: 150.00,
    required: true,
  })
  @IsNumber()
  @Min(0)
  quotedPrice: number;

  @ApiProperty({
    description: 'The scheduled date and time for the booking',
    example: '2024-12-20T10:00:00Z',
    required: true,
  })
  @IsDateString()
  scheduledDate: string;

  @ApiProperty({
    description: 'Additional notes about the quote',
    example: 'Price includes materials and labor',
    required: false,
  })
  @IsString()
  @IsOptional()
  notes?: string;
}
