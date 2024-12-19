import { MigrationInterface, QueryRunner } from 'typeorm';

export class UpdateBookingFields1702385864000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // First, check if the columns exist and create them if they don't
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

    // Then, update the column types
    await queryRunner.query(`
      ALTER TABLE bookings 
      ALTER COLUMN quoted_price TYPE decimal(10,2),
      ALTER COLUMN scheduled_date TYPE timestamp with time zone
    `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
      ALTER TABLE bookings 
      ALTER COLUMN quoted_price TYPE decimal,
      ALTER COLUMN scheduled_date TYPE timestamp
    `);
  }
}
