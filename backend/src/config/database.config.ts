import { TypeOrmModuleOptions } from '@nestjs/typeorm';
import { ConfigService } from '@nestjs/config';
import { User } from '../users/entities/user.entity';
import { Booking } from '../bookings/entities/booking.entity';

export const getDatabaseConfig = (configService: ConfigService): TypeOrmModuleOptions => {
  const databaseUrl = configService.get('DATABASE_URL');
  const isProduction = configService.get('NODE_ENV') === 'production';
  
  return {
    type: 'postgres',
    url: databaseUrl,
    entities: [User, Booking],
    migrations: [__dirname + '/../migrations/*{.ts,.js}'],
    migrationsRun: true,
    synchronize: !isProduction, // Only synchronize in non-production environments
    logging: !isProduction, // Only log in non-production environments
    ssl: {
      rejectUnauthorized: false
    },
    timezone: 'UTC',
    extra: {
      timezone: 'UTC'
    }
  } as TypeOrmModuleOptions;
};
