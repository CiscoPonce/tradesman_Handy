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
        "quotedPrice" DECIMAL(10,2),
        "scheduledDate" TIMESTAMPTZ,
        "clientId" UUID NOT NULL,
        "tradesmanId" UUID,
        location VARCHAR NOT NULL,
        "housingAssociationRef" VARCHAR,
        "preferredDate" TIMESTAMPTZ,
        "createdAt" TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
        "updatedAt" TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
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

    // Then, check if we need to rename any snake_case columns to camelCase
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Check and rename quoted_price to quotedPrice
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'quoted_price'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "quoted_price" TO "quotedPrice";
        END IF;

        -- Check and rename scheduled_date to scheduledDate
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'scheduled_date'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "scheduled_date" TO "scheduledDate";
        END IF;

        -- Check and rename client_id to clientId
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'client_id'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "client_id" TO "clientId";
        END IF;

        -- Check and rename tradesman_id to tradesmanId
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'tradesman_id'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "tradesman_id" TO "tradesmanId";
        END IF;

        -- Check and rename housing_association_ref to housingAssociationRef
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'housing_association_ref'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "housing_association_ref" TO "housingAssociationRef";
        END IF;

        -- Check and rename preferred_date to preferredDate
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'preferred_date'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "preferred_date" TO "preferredDate";
        END IF;

        -- Check and rename created_at to createdAt
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'created_at'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "created_at" TO "createdAt";
        END IF;

        -- Check and rename updated_at to updatedAt
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'updated_at'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "updated_at" TO "updatedAt";
        END IF;
      END $$;
    `);

    // Finally, update column types for all columns
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Update quotedPrice type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'quotedPrice') THEN
          ALTER TABLE bookings ALTER COLUMN "quotedPrice" TYPE decimal(10,2);
        END IF;

        -- Update scheduledDate type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'scheduledDate') THEN
          ALTER TABLE bookings ALTER COLUMN "scheduledDate" TYPE timestamp with time zone;
        END IF;

        -- Update createdAt type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'createdAt') THEN
          ALTER TABLE bookings ALTER COLUMN "createdAt" TYPE timestamp with time zone;
        END IF;

        -- Update updatedAt type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'updatedAt') THEN
          ALTER TABLE bookings ALTER COLUMN "updatedAt" TYPE timestamp with time zone;
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
