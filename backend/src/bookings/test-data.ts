import { CreateBookingDto } from './dto/create-booking.dto';
import { BookingSource, BookingStatus } from './entities/booking.entity';

const TRADESMAN_ID = '2b6fe808-b73b-4d16-915b-298b4d076c47';
const CLIENT_ID = '45993914-7aeb-4111-ac7f-5d168b03b0fb';

const generateRandomDate = (start: Date, end: Date) => {
  return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
};

const locations = [
  'London, UK',
  'Manchester, UK',
  'Birmingham, UK',
  'Leeds, UK',
  'Liverpool, UK',
  'Bristol, UK',
  'Newcastle, UK',
  'Sheffield, UK',
  'Nottingham, UK',
  'Glasgow, UK'
];

const jobTitles = [
  'Leaking Tap Repair',
  'Boiler Service',
  'Radiator Installation',
  'Bathroom Renovation',
  'Kitchen Plumbing',
  'Pipe Repair',
  'Water Heater Installation',
  'Drain Cleaning',
  'Toilet Repair',
  'Shower Installation'
];

const jobDescriptions = [
  'Kitchen tap has been leaking for a few days, needs urgent repair',
  'Annual boiler service and maintenance check required',
  'Need to install a new radiator in the living room',
  'Complete bathroom renovation including new fixtures and plumbing',
  'Kitchen sink and dishwasher installation needed',
  'Pipe burst under the sink, needs immediate repair',
  'Old water heater replacement with a new energy-efficient model',
  'Blocked drain causing slow drainage in bathroom',
  'Toilet not flushing properly, needs repair or replacement',
  'New shower installation in the master bathroom'
];

export const generatePendingBookings = (): CreateBookingDto[] => {
  return Array(5).fill(null).map(() => ({
    title: jobTitles[Math.floor(Math.random() * jobTitles.length)],
    description: jobDescriptions[Math.floor(Math.random() * jobDescriptions.length)],
    location: locations[Math.floor(Math.random() * locations.length)],
    clientId: CLIENT_ID,
    tradesmanId: TRADESMAN_ID,
    source: BookingSource.LOCAL,
    preferredDate: generateRandomDate(new Date(), new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)),
    quotedPrice: null,
    scheduledDate: null
  }));
};

export const generateAcceptedBookings = (): CreateBookingDto[] => {
  return Array(5).fill(null).map(() => {
    const preferredDate = generateRandomDate(new Date(), new Date(Date.now() + 30 * 24 * 60 * 60 * 1000));
    return {
      title: jobTitles[Math.floor(Math.random() * jobTitles.length)],
      description: jobDescriptions[Math.floor(Math.random() * jobDescriptions.length)],
      location: locations[Math.floor(Math.random() * locations.length)],
      clientId: CLIENT_ID,
      tradesmanId: TRADESMAN_ID,
      source: BookingSource.LOCAL,
      preferredDate,
      quotedPrice: Math.floor(Math.random() * 500) + 100, // Random price between 100 and 600
      scheduledDate: new Date(preferredDate.getTime() + Math.random() * 24 * 60 * 60 * 1000) // Schedule within 24 hours of preferred date
    };
  });
};

export const generateCompletedBookings = (): CreateBookingDto[] => {
  return Array(5).fill(null).map(() => {
    const preferredDate = generateRandomDate(new Date(Date.now() - 30 * 24 * 60 * 60 * 1000), new Date());
    return {
      title: jobTitles[Math.floor(Math.random() * jobTitles.length)],
      description: jobDescriptions[Math.floor(Math.random() * jobDescriptions.length)],
      location: locations[Math.floor(Math.random() * locations.length)],
      clientId: CLIENT_ID,
      tradesmanId: TRADESMAN_ID,
      source: BookingSource.LOCAL,
      preferredDate,
      quotedPrice: Math.floor(Math.random() * 500) + 100, // Random price between 100 and 600
      scheduledDate: new Date(preferredDate.getTime() + Math.random() * 24 * 60 * 60 * 1000) // Schedule within 24 hours of preferred date
    };
  });
};
