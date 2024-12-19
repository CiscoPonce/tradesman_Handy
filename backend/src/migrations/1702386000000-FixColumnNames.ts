import { MigrationInterface, QueryRunner } from 'typeorm';

export class FixColumnNames1702386000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // First, check if the columns exist and rename them
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Check and rename quotedPrice to quoted_price
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'quotedPrice'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "quotedPrice" TO quoted_price;
        END IF;

        -- Check and rename scheduledDate to scheduled_date
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'scheduledDate'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "scheduledDate" TO scheduled_date;
        END IF;

        -- Check and rename clientId to client_id
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'clientId'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "clientId" TO client_id;
        END IF;

        -- Check and rename tradesmanId to tradesman_id
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'tradesmanId'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "tradesmanId" TO tradesman_id;
        END IF;

        -- Check and rename housingAssociationRef to housing_association_ref
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'housingAssociationRef'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "housingAssociationRef" TO housing_association_ref;
        END IF;

        -- Check and rename preferredDate to preferred_date
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'preferredDate'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "preferredDate" TO preferred_date;
        END IF;

        -- Check and rename createdAt to created_at
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'createdAt'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "createdAt" TO created_at;
        END IF;

        -- Check and rename updatedAt to updated_at
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'updatedAt'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN "updatedAt" TO updated_at;
        END IF;
      END $$;
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

    // Revert the column names back to camelCase
    await queryRunner.query(`
      DO $$ 
      BEGIN 
        -- Check and rename quoted_price back to quotedPrice
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'quoted_price'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN quoted_price TO "quotedPrice";
        END IF;

        -- Check and rename scheduled_date back to scheduledDate
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'scheduled_date'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN scheduled_date TO "scheduledDate";
        END IF;

        -- Check and rename client_id back to clientId
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'client_id'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN client_id TO "clientId";
        END IF;

        -- Check and rename tradesman_id back to tradesmanId
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'tradesman_id'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN tradesman_id TO "tradesmanId";
        END IF;

        -- Check and rename housing_association_ref back to housingAssociationRef
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'housing_association_ref'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN housing_association_ref TO "housingAssociationRef";
        END IF;

        -- Check and rename preferred_date back to preferredDate
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'preferred_date'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN preferred_date TO "preferredDate";
        END IF;

        -- Check and rename created_at back to createdAt
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'created_at'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN created_at TO "createdAt";
        END IF;

        -- Check and rename updated_at back to updatedAt
        IF EXISTS (
          SELECT 1 FROM information_schema.columns 
          WHERE table_name = 'bookings' AND column_name = 'updated_at'
        ) THEN
          ALTER TABLE bookings RENAME COLUMN updated_at TO "updatedAt";
        END IF;
      END $$;
    `);
  }
}
