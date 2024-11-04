# Task 3
## 3.3.3 Endpoint Outputs

### Create a new Trip
```json
{
  "id": 4,
  "name": "New York City Tour",
  "price": 15.5,
  "category": "HISTORY",
  "guide": null,
  "start_time": "09:00",
  "end_time": "15:30",
  "start_position": "Times Square, New York"
}
```

### Add a Guide to the Trip
``` text
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:49:51 GMT
Content-Type: application/json
Content-Length: 0

<Response body is empty>
```

### Get trip by id: 3
```json
{
  "id": 3,
  "name": "Food Tasting",
  "price": 79.99,
  "category": "CULINARY",
  "guide": null,
  "start_time": "10:15",
  "end_time": "14:30",
  "start_position": "Chicago"
}
```

### Get all Trips
```json
[
  {
    "id": 2,
    "name": "Beach Getaway",
    "price": 149.99,
    "category": "RELAXATION",
    "guide": null,
    "start_time": "09:00",
    "end_time": "17:00",
    "start_position": "Los Angeles"
  },
  {
    "id": 3,
    "name": "Food Tasting",
    "price": 79.99,
    "category": "CULINARY",
    "guide": null,
    "start_time": "10:15",
    "end_time": "14:30",
    "start_position": "Chicago"
  },
  {
    "id": 1,
    "name": "City Tour",
    "price": 99.99,
    "category": "SIGHTSEEING",
    "guide": null,
    "start_time": "08:30",
    "end_time": "12:00",
    "start_position": "New York"
  }
]
```

### Delete a trip: 4
``` text
HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 10:15:10 GMT
Content-Type: application/json

<Response body is empty>
```

### Update a trip by id: 1
```json
{
  "id": null,
  "name": "City Tour",
  "price": 100.0,
  "category": "SIGHTSEEING",
  "guide": null,
  "start_time": "09:00",
  "end_time": "12:30",
  "start_position": "New York"
}
```

### Add a Guide to the Trip
``` text
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:21:35 GMT
Content-Type: application/json
Content-Length: 0

<Response body is empty>
```

## 3.3.5
### Why do we suggest a PUT method for adding a guide to a trip instead of a POST method?
We are not interested in creating a whole new trip object in the database when we want to add a guide.
Therefore, it is best to update the existing trip object with the guide information using the PUT method.


