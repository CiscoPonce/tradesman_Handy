import { MigrationInterface, QueryRunner } from 'typeorm';

export class UpdateBookingFields1702385864000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
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
