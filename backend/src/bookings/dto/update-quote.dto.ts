import { IsNumber, IsOptional, IsString, Min } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class UpdateQuoteDto {
  @ApiProperty({
    description: 'The quoted price for the booking',
    example: 150.00,
    required: true,
  })
  @IsNumber()
  @Min(0)
  quotedPrice: number;

  @ApiProperty({
    description: 'Additional notes about the quote',
    example: 'Price includes materials and labor',
    required: false,
  })
  @IsString()
  @IsOptional()
  notes?: string;
}
