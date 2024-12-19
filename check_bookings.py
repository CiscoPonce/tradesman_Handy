import requests
from datetime import datetime, timedelta

# Use Render URL for testing
BASE_URL = "https://tradesman-handy.onrender.com/api/v1"

# John Smith's ID (tradesman)
JOHN_ID = "2b6fe808-b73b-4d16-915b-298b4d076c47"

def get_bookings():
    response = requests.get(f"{BASE_URL}/bookings/tradesman/{JOHN_ID}")
    if response.status_code != 200:
        print(f"Failed to get bookings: {response.text}")
        return None
    return response.json()

def update_booking(booking_id, price=None, scheduled_date=None):
    update_data = {}
    if price is not None:
        update_data['quotedPrice'] = str(price)
    if scheduled_date is not None:
        update_data['scheduledDate'] = scheduled_date.isoformat() + 'Z'
    
    response = requests.patch(f"{BASE_URL}/bookings/{booking_id}", json=update_data)
    if response.status_code != 200:
        print(f"Failed to update booking {booking_id}: {response.text}")
        return False
    return True

def update_pending_bookings():
    bookings = get_bookings()
    if not bookings:
        return
    
    # Get next week's dates
    today = datetime.utcnow()
    next_week = today + timedelta(days=7)
    
    # Set different times for different types of jobs
    morning_time = next_week.replace(hour=9, minute=0, second=0, microsecond=0)
    afternoon_time = next_week.replace(hour=14, minute=0, second=0, microsecond=0)
    
    for booking in bookings:
        if booking['status'] == 'quoted' and not booking['quotedPrice']:
            booking_id = booking['id']
            title = booking['title'].lower()
            
            # Set price and time based on job type
            if 'renovation' in title:
                price = 2500.00
                scheduled_time = morning_time
            elif 'installation' in title:
                price = 500.00
                scheduled_time = afternoon_time
            elif 'repair' in title or 'fix' in title:
                price = 200.00
                scheduled_time = morning_time
            else:
                price = 350.00
                scheduled_time = afternoon_time
            
            print(f"\nUpdating booking: {title}")
            print(f"Setting price: £{price}")
            print(f"Scheduling for: {scheduled_time.strftime('%Y-%m-%d %H:%M')}")
            
            if update_booking(booking_id, price=price, scheduled_date=scheduled_time):
                print("✓ Successfully updated")
            else:
                print("✗ Update failed")

def display_bookings():
    bookings = get_bookings()
    if not bookings:
        return
    
    print("\nCurrent Bookings:")
    print("-" * 50)
    
    for booking in bookings:
        print(f"\n[{booking.get('status', 'Unknown Status').upper()}]")
        print(f"Title: {booking.get('title', 'No Title')}")
        
        # Check scheduled date
        scheduled_date = booking.get('scheduledDate')
        if scheduled_date:
            try:
                date_obj = datetime.strptime(scheduled_date, "%Y-%m-%dT%H:%M:%S.%fZ")
                print(f"Date: {date_obj.strftime('%d/%m/%Y, %H:%M')}")
            except ValueError:
                print(f"Date: {scheduled_date} (Invalid format)")
        else:
            print("Date: Not set")
        
        # Check price
        quoted_price = booking.get('quotedPrice')
        if quoted_price is not None:
            print(f"Price: £{quoted_price}")
        else:
            print("Price: Not set")
            
        print(f"Location: {booking.get('location', 'No Location')}")
        print("-" * 50)

if __name__ == "__main__":
    print("Updating pending bookings...")
    update_pending_bookings()
    
    print("\nDisplaying all bookings after update...")
    display_bookings()
