#!/bin/bash

API_URL="https://tradesman-handy.onrender.com/api/v1"
TRADESMAN_ID="2b6fe808-b73b-4d16-915b-298b4d076c47"
CLIENT_ID="45993914-7aeb-4111-ac7f-5d168b03b0fb"

# Create pending bookings
echo "Creating pending bookings..."
curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Leaking Tap Repair\",\"description\":\"Kitchen tap has been leaking for a few days, needs urgent repair\",\"location\":\"London, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-01-05T10:00:00Z\"}"
curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Boiler Service\",\"description\":\"Annual boiler service and maintenance check required\",\"location\":\"Manchester, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-01-10T14:00:00Z\"}"
curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Radiator Installation\",\"description\":\"Need to install a new radiator in the living room\",\"location\":\"Birmingham, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-01-15T09:00:00Z\"}"
curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Bathroom Renovation\",\"description\":\"Complete bathroom renovation including new fixtures and plumbing\",\"location\":\"Leeds, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-01-20T11:00:00Z\"}"
curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Kitchen Plumbing\",\"description\":\"Kitchen sink and dishwasher installation needed\",\"location\":\"Liverpool, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-01-25T13:00:00Z\"}"

# Create and accept bookings
echo "Creating accepted bookings..."
# Booking 1
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Pipe Repair\",\"description\":\"Pipe burst under the sink, needs immediate repair\",\"location\":\"Bristol, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-02-05T10:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/accept" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"quotedPrice\":150}"

# Booking 2
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Water Heater Installation\",\"description\":\"Old water heater replacement with a new energy-efficient model\",\"location\":\"Newcastle, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-02-10T14:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/accept" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"quotedPrice\":300}"

# Booking 3
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Drain Cleaning\",\"description\":\"Blocked drain causing slow drainage in bathroom\",\"location\":\"Sheffield, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-02-15T09:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/accept" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"quotedPrice\":100}"

# Booking 4
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Toilet Repair\",\"description\":\"Toilet not flushing properly, needs repair or replacement\",\"location\":\"Nottingham, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-02-20T11:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/accept" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"quotedPrice\":200}"

# Booking 5
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Shower Installation\",\"description\":\"New shower installation in the master bathroom\",\"location\":\"Glasgow, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2024-02-25T13:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/accept" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"quotedPrice\":250}"

# Create and complete bookings
echo "Creating completed bookings..."
# Booking 1
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Emergency Pipe Fix\",\"description\":\"Fixed burst pipe in basement\",\"location\":\"London, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2023-12-01T10:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/status" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"status\":\"completed\",\"quotedPrice\":180}"

# Booking 2
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Boiler Replacement\",\"description\":\"Replaced old boiler with new efficient model\",\"location\":\"Manchester, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2023-12-05T14:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/status" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"status\":\"completed\",\"quotedPrice\":1200}"

# Booking 3
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Bathroom Remodel\",\"description\":\"Complete bathroom renovation\",\"location\":\"Birmingham, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2023-12-10T09:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/status" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"status\":\"completed\",\"quotedPrice\":3500}"

# Booking 4
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Kitchen Sink Install\",\"description\":\"Installed new kitchen sink and taps\",\"location\":\"Leeds, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2023-12-15T11:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/status" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"status\":\"completed\",\"quotedPrice\":400}"

# Booking 5
response=$(curl -X POST "$API_URL/bookings" -H "Content-Type: application/json" -d "{\"title\":\"Heating System Service\",\"description\":\"Annual heating system maintenance\",\"location\":\"Liverpool, UK\",\"clientId\":\"$CLIENT_ID\",\"tradesmanId\":\"$TRADESMAN_ID\",\"source\":\"local\",\"preferredDate\":\"2023-12-20T13:00:00Z\"}")
booking_id=$(echo $response | jq -r '.id')
curl -X PUT "$API_URL/bookings/$booking_id/status" -H "Content-Type: application/json" -d "{\"tradesmanId\":\"$TRADESMAN_ID\",\"status\":\"completed\",\"quotedPrice\":150}"

echo "All bookings created successfully!"
