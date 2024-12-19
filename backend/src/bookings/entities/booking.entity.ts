import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, JoinColumn } from 'typeorm';
import { User } from '../../users/entities/user.entity';

export enum BookingStatus {
  PENDING = 'pending',
  QUOTED = 'quoted',
  ACCEPTED = 'accepted',
  REJECTED = 'rejected',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled'
}

export enum BookingSource {
  LOCAL = 'local',
  HOUSING_ASSOCIATION = 'housing_association'
}

@Entity({ name: 'bookings' })
export class Booking {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column()
  title: string;

  @Column()
  description: string;

  @Column({
    type: 'enum',
    enum: BookingSource,
    default: BookingSource.LOCAL
  })
  source: BookingSource;

  @Column({
    type: 'enum',
    enum: BookingStatus,
    default: BookingStatus.PENDING
  })
  status: BookingStatus;

  @Column({
    type: 'decimal',
    precision: 10,
    scale: 2,
    nullable: true,
    name: 'quoted_price'
  })
  quotedPrice: number;

  @Column({
    type: 'timestamptz',
    nullable: true,
    name: 'scheduled_date'
  })
  scheduledDate: Date;

  @Column({ name: 'client_id', type: 'uuid' })
  clientId: string;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'client_id', referencedColumnName: 'id' })
  client: User;

  @Column({
    name: 'tradesman_id',
    type: 'uuid',
    nullable: true
  })
  tradesmanId: string;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'tradesman_id', referencedColumnName: 'id' })
  tradesman: User;

  @Column()
  location: string;

  @Column({
    nullable: true,
    name: 'housing_association_ref'
  })
  housingAssociationRef: string;

  @Column({
    type: 'timestamptz',
    nullable: true,
    name: 'preferred_date'
  })
  preferredDate: Date;

  @CreateDateColumn({
    type: 'timestamptz',
    name: 'created_at'
  })
  createdAt: Date;

  @UpdateDateColumn({
    type: 'timestamptz',
    name: 'updated_at'
  })
  updatedAt: Date;
}
