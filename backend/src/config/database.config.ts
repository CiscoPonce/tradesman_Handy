import { TypeOrmModuleOptions } from '@nestjs/typeorm';
import { ConfigService } from '@nestjs/config';
import { User } from '../users/entities/user.entity';
import { Booking } from '../bookings/entities/booking.entity';

export const getDatabaseConfig = (configService: ConfigService): TypeOrmModuleOptions => {
  const databaseUrl = configService.get('DATABASE_URL');
  
  return {
    type: 'postgres',
    url: databaseUrl,
    entities: [User, Booking],
    synchronize: configService.get('NODE_ENV') !== 'production',
    logging: configService.get('NODE_ENV') !== 'production',
    ssl: {
      rejectUnauthorized: false
    }
  } as TypeOrmModuleOptions;
};
