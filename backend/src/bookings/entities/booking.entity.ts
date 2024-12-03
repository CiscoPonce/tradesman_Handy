import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, JoinColumn } from 'typeorm';
import { User } from '../../users/entities/user.entity';

export enum BookingStatus {
  PENDING = 'pending',
  QUOTED = 'quoted',
  ACCEPTED = 'accepted',
  REJECTED = 'rejected',
  IN_PROGRESS = 'in_progress',
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

  @Column('text')
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

  @Column('decimal', { precision: 10, scale: 2, nullable: true })
  quotedPrice: number;

  @Column({ type: 'timestamp', nullable: true })
  scheduledDate: Date;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'clientId' })
  client: User;

  @Column()
  clientId: string;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'tradesmanId' })
  tradesman: User;

  @Column()
  tradesmanId: string;

  @Column({ nullable: true })
  location: string;

  @Column({ nullable: true })
  housingAssociationRef: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
