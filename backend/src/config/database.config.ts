import { TypeOrmModuleOptions } from '@nestjs/typeorm';
import { ConfigService } from '@nestjs/config';
import { User } from '../users/entities/user.entity';
import { Booking } from '../bookings/entities/booking.entity';

export const getDatabaseConfig = (configService: ConfigService): TypeOrmModuleOptions => {
  return {
    type: 'postgres',
    host: 'ep-patient-fog-a2kjx4by.eu-central-1.aws.neon.tech',
    port: 5432,
    username: 'handyman_owner',
    password: 'h8BJOiTxtP6d',
    database: 'handyman',
    entities: [User, Booking],
    synchronize: configService.get('NODE_ENV') !== 'production',
    logging: configService.get('NODE_ENV') !== 'production',
    ssl: {
      rejectUnauthorized: false
    }
  } as TypeOrmModuleOptions;
};
