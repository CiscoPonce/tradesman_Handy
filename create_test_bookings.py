import requests
from datetime import datetime, timedelta

# Use Render URL for testing
BASE_URL = "https://tradesman-handy.onrender.com/api/v1"

# Use existing users
JANE_ID = "45993914-7aeb-4111-ac7f-5d168b03b0fb"  # Jane Doe (client)
JOHN_ID = "2b6fe808-b73b-4d16-915b-298b4d076c47"  # John Smith (handyman)

# Current time from the provided timestamp
CURRENT_TIME = datetime.strptime("2024-12-19T10:20:20Z", "%Y-%m-%dT%H:%M:%SZ")

def create_booking(title, description, quoted_price, location, scheduled_date, tradesman_id, client_id):
    response = requests.post(f"{BASE_URL}/bookings", json={
        "title": title,
        "description": description,
        "source": "local",
        "quotedPrice": quoted_price,
        "scheduledDate": scheduled_date.strftime("%Y-%m-%dT%H:%M:%SZ"),
        "tradesmanId": tradesman_id,
        "clientId": client_id,
        "location": location
    })
    if response.status_code != 201:
        print(f"Failed to create booking: {response.text}")
        return None
    
    booking = response.json()
    # Print in the format shown in the mobile UI
    print("\n[Pending]")
    print(f"{title}")
    print(f"{scheduled_date.strftime('%d/%m, %H:%M')}")
    print(f"Location: {location}")
    print(f"£{quoted_price}")
    return booking

def create_test_bookings():
    # Create bookings
    bookings = [
        {
            "title": "One-time Fix",
            "description": "Fix a broken pipe",
            "quoted_price": 150,
            "location": "Thames Street, London",
            "scheduled_date": CURRENT_TIME.replace(hour=10, minute=0) + timedelta(days=7),  # Set to 10:00
            "tradesman_id": JOHN_ID,
            "client_id": JANE_ID
        },
        {
            "title": "Monthly Checkup - basement",
            "description": "Regular maintenance of basement plumbing",
            "quoted_price": 100,
            "location": "Thames Street, London",
            "scheduled_date": CURRENT_TIME.replace(hour=14, minute=0) + timedelta(days=14),  # Set to 14:00
            "tradesman_id": JOHN_ID,
            "client_id": JANE_ID
        },
        {
            "title": "Weekly Maintenance - 4 Flats",
            "description": "Weekly maintenance of plumbing in 4 flats",
            "quoted_price": 250,
            "location": "Thames Street, London",
            "scheduled_date": CURRENT_TIME.replace(hour=16, minute=0) + timedelta(days=21),  # Set to 16:00
            "tradesman_id": JOHN_ID,
            "client_id": JANE_ID
        }
    ]

    print("Creating test bookings...")
    for booking in bookings:
        created_booking = create_booking(**booking)
        if created_booking:
            print("-" * 40)  # Separator between bookings

if __name__ == "__main__":
    create_test_bookings()
