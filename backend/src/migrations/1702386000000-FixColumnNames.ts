import { MigrationInterface, QueryRunner } from 'typeorm';

export class FixColumnNames1702386000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // First, rename the columns to snake_case
    await queryRunner.query(`
      ALTER TABLE bookings 
      RENAME COLUMN "quotedPrice" TO quoted_price;
      
      ALTER TABLE bookings 
      RENAME COLUMN "scheduledDate" TO scheduled_date;
      
      ALTER TABLE bookings 
      RENAME COLUMN "clientId" TO client_id;
      
      ALTER TABLE bookings 
      RENAME COLUMN "tradesmanId" TO tradesman_id;
      
      ALTER TABLE bookings 
      RENAME COLUMN "housingAssociationRef" TO housing_association_ref;
      
      ALTER TABLE bookings 
      RENAME COLUMN "preferredDate" TO preferred_date;
      
      ALTER TABLE bookings 
      RENAME COLUMN "createdAt" TO created_at;
      
      ALTER TABLE bookings 
      RENAME COLUMN "updatedAt" TO updated_at;
    `);

    // Then update the column types
    await queryRunner.query(`
      ALTER TABLE bookings 
      ALTER COLUMN quoted_price TYPE decimal(10,2),
      ALTER COLUMN scheduled_date TYPE timestamp with time zone,
      ALTER COLUMN preferred_date TYPE timestamp with time zone,
      ALTER COLUMN created_at TYPE timestamp with time zone,
      ALTER COLUMN updated_at TYPE timestamp with time zone;
    `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // Revert the column types
    await queryRunner.query(`
      ALTER TABLE bookings 
      ALTER COLUMN quoted_price TYPE decimal,
      ALTER COLUMN scheduled_date TYPE timestamp,
      ALTER COLUMN preferred_date TYPE timestamp,
      ALTER COLUMN created_at TYPE timestamp,
      ALTER COLUMN updated_at TYPE timestamp;
    `);

    // Revert the column names to camelCase
    await queryRunner.query(`
      ALTER TABLE bookings 
      RENAME COLUMN quoted_price TO "quotedPrice";
      
      ALTER TABLE bookings 
      RENAME COLUMN scheduled_date TO "scheduledDate";
      
      ALTER TABLE bookings 
      RENAME COLUMN client_id TO "clientId";
      
      ALTER TABLE bookings 
      RENAME COLUMN tradesman_id TO "tradesmanId";
      
      ALTER TABLE bookings 
      RENAME COLUMN housing_association_ref TO "housingAssociationRef";
      
      ALTER TABLE bookings 
      RENAME COLUMN preferred_date TO "preferredDate";
      
      ALTER TABLE bookings 
      RENAME COLUMN created_at TO "createdAt";
      
      ALTER TABLE bookings 
      RENAME COLUMN updated_at TO "updatedAt";
    `);
  }
}
