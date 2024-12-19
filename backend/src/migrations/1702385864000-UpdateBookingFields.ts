import { MigrationInterface, QueryRunner } from 'typeorm';

export class UpdateBookingFields1702385864000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // First, create the bookings table if it doesn't exist
    await queryRunner.query(`
      CREATE TABLE IF NOT EXISTS bookings (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        title VARCHAR NOT NULL,
        description TEXT NOT NULL,
        source VARCHAR DEFAULT 'local',
        status VARCHAR DEFAULT 'pending',
        quoted_price DECIMAL(10,2),
        scheduled_date TIMESTAMPTZ,
        client_id UUID NOT NULL,
        tradesman_id UUID,
        location VARCHAR NOT NULL,
        housing_association_ref VARCHAR,
        preferred_date TIMESTAMPTZ,
        created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
      );

      -- Create enum types if they don't exist
      DO $$ 
      BEGIN 
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'bookingsource') THEN
          CREATE TYPE BookingSource AS ENUM ('local', 'housing_association');
        END IF;
        
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'bookingstatus') THEN
          CREATE TYPE BookingStatus AS ENUM ('pending', 'quoted', 'accepted', 'rejected', 'completed', 'cancelled');
        END IF;
      EXCEPTION
        WHEN duplicate_object THEN null;
      END $$;

      -- Update column types to use enums
      DO $$
      BEGIN
        ALTER TABLE bookings 
          ALTER COLUMN source TYPE BookingSource USING source::BookingSource,
          ALTER COLUMN status TYPE BookingStatus USING status::BookingStatus;
      EXCEPTION
        WHEN others THEN null;
      END $$;
    `);

    // Then, check if we need to rename any camelCase columns to snake_case
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Check and rename quotedPrice to quoted_price
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'quotedprice'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "quotedPrice" TO quoted_price;
        END IF;

        -- Check and rename scheduledDate to scheduled_date
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'scheduleddate'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "scheduledDate" TO scheduled_date;
        END IF;

        -- Check and rename clientId to client_id
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'clientid'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "clientId" TO client_id;
        END IF;

        -- Check and rename tradesmanId to tradesman_id
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'tradesmanid'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "tradesmanId" TO tradesman_id;
        END IF;

        -- Check and rename housingAssociationRef to housing_association_ref
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'housingassociationref'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "housingAssociationRef" TO housing_association_ref;
        END IF;

        -- Check and rename preferredDate to preferred_date
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'preferreddate'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "preferredDate" TO preferred_date;
        END IF;

        -- Check and rename createdAt to created_at
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'createdat'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "createdAt" TO created_at;
        END IF;

        -- Check and rename updatedAt to updated_at
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'updatedat'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "updatedAt" TO updated_at;
        END IF;
      END $$;
    `);

    // Finally, update column types for all columns
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Update quoted_price type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'quoted_price') THEN
          ALTER TABLE bookings ALTER COLUMN quoted_price TYPE decimal(10,2);
        END IF;

        -- Update scheduled_date type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'scheduled_date') THEN
          ALTER TABLE bookings ALTER COLUMN scheduled_date TYPE timestamp with time zone;
        END IF;

        -- Update created_at type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'created_at') THEN
          ALTER TABLE bookings ALTER COLUMN created_at TYPE timestamp with time zone;
        END IF;

        -- Update updated_at type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'updated_at') THEN
          ALTER TABLE bookings ALTER COLUMN updated_at TYPE timestamp with time zone;
        END IF;
      END $$;
    `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // Drop the bookings table and its dependencies
    await queryRunner.query(`
      DROP TABLE IF EXISTS bookings;
      DROP TYPE IF EXISTS BookingSource;
      DROP TYPE IF EXISTS BookingStatus;
    `);
  }
}
