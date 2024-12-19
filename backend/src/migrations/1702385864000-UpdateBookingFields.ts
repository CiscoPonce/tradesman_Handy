import { MigrationInterface, QueryRunner } from 'typeorm';

export class UpdateBookingFields1702385864000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // First, check if we need to rename any camelCase columns to snake_case
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

    // Then, add missing columns if they don't exist
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'quoted_price') THEN
          ALTER TABLE bookings ADD COLUMN quoted_price decimal(10,2);
        END IF;
        
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'scheduled_date') THEN
          ALTER TABLE bookings ADD COLUMN scheduled_date timestamp with time zone;
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
    // Revert column types
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Revert quoted_price type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'quoted_price') THEN
          ALTER TABLE bookings ALTER COLUMN quoted_price TYPE decimal;
        END IF;

        -- Revert scheduled_date type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'scheduled_date') THEN
          ALTER TABLE bookings ALTER COLUMN scheduled_date TYPE timestamp;
        END IF;

        -- Revert created_at type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'created_at') THEN
          ALTER TABLE bookings ALTER COLUMN created_at TYPE timestamp;
        END IF;

        -- Revert updated_at type
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'updated_at') THEN
          ALTER TABLE bookings ALTER COLUMN updated_at TYPE timestamp;
        END IF;
      END $$;
    `);

    // Revert column names back to camelCase
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Check and rename quoted_price back to quotedPrice
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'quoted_price') THEN
          ALTER TABLE bookings RENAME COLUMN quoted_price TO "quotedPrice";
        END IF;

        -- Check and rename scheduled_date back to scheduledDate
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'scheduled_date') THEN
          ALTER TABLE bookings RENAME COLUMN scheduled_date TO "scheduledDate";
        END IF;

        -- Check and rename client_id back to clientId
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'client_id') THEN
          ALTER TABLE bookings RENAME COLUMN client_id TO "clientId";
        END IF;

        -- Check and rename tradesman_id back to tradesmanId
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'tradesman_id') THEN
          ALTER TABLE bookings RENAME COLUMN tradesman_id TO "tradesmanId";
        END IF;

        -- Check and rename housing_association_ref back to housingAssociationRef
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'housing_association_ref') THEN
          ALTER TABLE bookings RENAME COLUMN housing_association_ref TO "housingAssociationRef";
        END IF;

        -- Check and rename preferred_date back to preferredDate
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'preferred_date') THEN
          ALTER TABLE bookings RENAME COLUMN preferred_date TO "preferredDate";
        END IF;

        -- Check and rename created_at back to createdAt
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'created_at') THEN
          ALTER TABLE bookings RENAME COLUMN created_at TO "createdAt";
        END IF;

        -- Check and rename updated_at back to updatedAt
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'updated_at') THEN
          ALTER TABLE bookings RENAME COLUMN updated_at TO "updatedAt";
        END IF;
      END $$;
    `);

    // Drop columns if they exist
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'quoted_price') THEN
          ALTER TABLE bookings DROP COLUMN quoted_price;
        END IF;

        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'bookings' AND column_name = 'scheduled_date') THEN
          ALTER TABLE bookings DROP COLUMN scheduled_date;
        END IF;
      END $$;
    `);
  }
}
