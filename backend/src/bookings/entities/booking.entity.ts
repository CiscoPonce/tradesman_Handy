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

@Entity('bookings')
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
    name: 'quotedPrice'
  })
  quotedPrice: number;

  @Column({
    type: 'timestamptz',
    nullable: true,
    name: 'scheduledDate'
  })
  scheduledDate: Date;

  @Column({ name: 'clientId', type: 'uuid' })
  clientId: string;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'clientId', referencedColumnName: 'id' })
  client: User;

  @Column({
    name: 'tradesmanId',
    type: 'uuid',
    nullable: true
  })
  tradesmanId: string;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'tradesmanId', referencedColumnName: 'id' })
  tradesman: User;

  @Column()
  location: string;

  @Column({
    nullable: true,
    name: 'housingAssociationRef'
  })
  housingAssociationRef: string;

  @Column({
    type: 'timestamptz',
    nullable: true,
    name: 'preferredDate'
  })
  preferredDate: Date;

  @CreateDateColumn({
    type: 'timestamptz',
    name: 'createdAt'
  })
  createdAt: Date;

  @UpdateDateColumn({
    type: 'timestamptz',
    name: 'updatedAt'
  })
  updatedAt: Date;
}
