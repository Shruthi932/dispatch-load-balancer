
# Dispatch Load Balancer

A Java-based backend application that intelligently assigns delivery orders to fleet vehicles based on order priority, vehicle capacity, and delivery distance.

## Project Overview

The Dispatch Load Balancer helps optimize delivery assignments by selecting suitable vehicles for incoming orders.

The application processes delivery orders and available fleet vehicles, then generates a dispatch plan while considering:

- **Order Priority:** High-priority orders are assigned before medium- and low-priority orders.
- **Vehicle Capacity:** Orders are assigned only when the vehicle has sufficient remaining capacity.
- **Distance Optimization:** The system selects the nearest feasible vehicle using the Haversine formula.
- **Unassigned Orders:** Orders that cannot be assigned to any available vehicle are reported separately.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot | Building REST APIs |
| Spring Data JPA | Database operations |
| Hibernate | ORM (Object-Relational Mapping) |
| MySQL | Relational database |
| Maven | Project build and dependency management |
| Swagger / OpenAPI | API documentation and testing |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies during unit testing |
| Postman | API testing |
| IntelliJ IDEA | Development environment |

## Key Features

- RESTful APIs for managing delivery orders and fleet vehicles.
- Priority-based order assignment.
- Vehicle capacity validation.
- Distance calculation using the Haversine formula.
- Nearest feasible vehicle selection.
- Identification of unassigned orders.
- Input validation and centralized exception handling.
- Automated unit testing.


## Project Architecture

The application follows a layered architecture to separate responsibilities and improve maintainability.

```text

dispatch-load-balancer/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/freightfox/dispatch/
│   │   │       ├── controller/
│   │   │       │   └── DispatchController.java
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       ├── entity/
│   │   │       │   ├── DeliveryOrder.java
│   │   │       │   └── FleetVehicle.java
│   │   │       ├── enums/
│   │   │       │   └── OrderPriority.java
│   │   │       ├── exception/
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── repository/
│   │   │       │   ├── DeliveryOrderRepository.java
│   │   │       │   └── FleetVehicleRepository.java
│   │   │       ├── service/
│   │   │       │   ├── DispatchInputService.java
│   │   │       │   └── DispatchOptimizationService.java
│   │   │       ├── util/
│   │   │       │   └── DistanceCalculator.java
│   │   │       └── DispatchLoadBalancerApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── com/freightfox/dispatch/
│               ├── service/
│               │   └── DispatchOptimizationServiceTest.java
│               ├── util/
│               │   └── DistanceCalculatorTest.java
│               └── DispatchLoadBalancerApplicationTests.java
│
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
└── pom.xml


### Layer Responsibilities

- **Controller:** Handles incoming HTTP requests and returns API responses.
- **Service:** Contains business logic for saving input data and generating dispatch plans.
- **Repository:** Communicates with the MySQL database using Spring Data JPA.
- **Entity:** Represents the database tables for delivery orders and fleet vehicles.
- **DTO:** Transfers request and response data between the client and application.
- **Enums:** Defines supported order priorities: HIGH, MEDIUM, and LOW.
- **Utility:** Calculates geographical distance using the Haversine formula.
- **Exception:** Handles validation errors, invalid JSON, and unexpected exceptions.


## REST API Endpoints

Base URL: `http://localhost:8086`

### 1. Add Delivery Orders

- **Method:** POST
- **Endpoint:** `/api/dispatch/orders`
- **Description:** Accepts and stores delivery order details.

**Sample Request:**

```json
{
  "orders": [
    {
      "orderId": "ORD101",
      "latitude": 12.9716,
      "longitude": 77.5946,
      "address": "MG Road, Bangalore",
      "packageWeight": 10.0,
      "priority": "HIGH"
    }
  ]
}
```

### 2. Add Fleet Vehicles

- **Method:** POST
- **Endpoint:** `/api/dispatch/vehicles`
- **Description:** Accepts and stores fleet vehicle details.

**Sample Request:**

```json
{
  "vehicles": [
    {
      "vehicleId": "VEH101",
      "capacity": 100.0,
      "currentLatitude": 12.9352,
      "currentLongitude": 77.6245,
      "currentAddress": "Koramangala, Bangalore"
    }
  ]
}
```

### 3. Generate Dispatch Plan

- **Method:** GET
- **Endpoint:** `/api/dispatch/plan`
- **Description:** Generates a dispatch plan by assigning orders to suitable vehicles based on priority, capacity, and distance.


**Sample Response:**

```json
{
  "dispatchPlan": [
    {
      "vehicleId": "NEARTEST001",
      "capacity": 20,
      "totalLoad": 0,
      "totalDistance": "0.00 km",
      "assignedOrders": []
    },
    {
      "vehicleId": "NEARTEST002",
      "capacity": 100,
      "totalLoad": 90,
      "totalDistance": "15.73 km",
      "assignedOrders": [
        {
          "orderId": "NEARORDER001",
          "address": "Nearest Vehicle Test Location",
          "packageWeight": 90,
          "priority": "HIGH"
        }
      ]
    }
  ],
  "unassignedOrders": [
    "ORD004"
  ]
}
```

*Note: This is a shortened example using actual values from the API response. The complete response also includes the other vehicles and their assignments.*

The `unassignedOrders` field contains orders that could not be assigned because no available vehicle had sufficient remaining capacity.


### API Documentation Interface

Swagger UI: `http://localhost:8086/swagger-ui/index.html`


## Error Handling and Validation

The application uses Spring Boot validation annotations and a centralized exception handler (`@RestControllerAdvice`) to handle invalid requests and unexpected errors.

### Validation Rules

| Field | Validation Rule |
|---|---|
| Order ID | Must not be blank |
| Vehicle ID | Must not be blank |
| Latitude | Must be between -90 and 90 |
| Longitude | Must be between -180 and 180 |
| Package Weight | Must be greater than 0 |
| Vehicle Capacity | Must be greater than 0 |
| Priority | Must be HIGH, MEDIUM, or LOW |
| Orders List | Must not be empty |
| Vehicles List | Must not be empty |

### HTTP Status Codes

| Status Code | Description |
|---|---|
| 200 OK | Request processed successfully |
| 400 Bad Request | Invalid input, validation failure, or malformed JSON |
| 404 Not Found | Requested endpoint or resource not found |
| 500 Internal Server Error | Unexpected server-side error |

### Sample Validation Error Response

**Example:** Submitting a vehicle with negative capacity.

```json
{
  "message": "vehicles[0].capacity: must be greater than 0.0",
  "status": "error"
}
```

The application returns HTTP 400 Bad Request for this validation failure.

Unexpected exceptions are handled centrally and return a generic error response to avoid exposing internal implementation details.


## Dispatch Optimization Algorithm

The application uses a priority-based greedy assignment approach to allocate delivery orders to available fleet vehicles.


### Assignment Process

1. Retrieve all delivery orders and available fleet vehicles from the database.
2. Sort delivery orders by priority: HIGH, MEDIUM, then LOW.
3. For each order, examine the available vehicles.
4. Check whether each vehicle has sufficient remaining capacity.
5. Calculate the distance from each feasible vehicle's current location to the delivery location using the Haversine formula.
6. Select the feasible vehicle with the shortest distance.
7. Assign the order and update the vehicle's load and current location in the in-memory calculation.
8. If no vehicle has sufficient capacity, add the order to the unassigned orders list.
9. Generate the final dispatch plan.

### Order Processing and Route Calculation

- Orders are sorted by priority. No additional explicit sorting by weight or order ID is applied within the same priority.
- Assigned delivery stops follow the order assignment sequence; they are not re-sequenced afterward.
- Total distance is calculated as the sum of travel legs from the vehicle's starting location through its assigned delivery locations.
- The distance calculation does not include a return trip to the starting location.
- Vehicle load and location updates are maintained in memory during plan generation and are not saved to the database.

### Time Complexity

Let:
- n = Number of delivery orders.
- m = Number of fleet vehicles.

The approximate time complexity is:

O(n log n + m log m + n × m)

This includes sorting the orders and vehicles and checking available vehicles for each order.


### Distance Calculation

The application uses the Haversine formula to calculate the great-circle distance between two geographical points based on their latitude and longitude.

**Formula:**

a = sin²(Δφ / 2) + cos(φ1) × cos(φ2) × sin²(Δλ / 2)

c = 2 × atan2(√a, √(1 - a))

d = R × c

Where:

- φ1 = Latitude of the first location in radians.
- φ2 = Latitude of the second location in radians.
- Δφ = Difference between the latitudes in radians.
- Δλ = Difference between the longitudes in radians.
- R = Earth's mean radius (6371 km).
- d = Distance between the two locations in kilometers.

The calculated distance is used to identify the nearest feasible vehicle for each delivery order.

### Assignment Rules

- HIGH-priority orders are processed before MEDIUM- and LOW-priority orders.
- Vehicle capacity must never be exceeded.
- Among feasible vehicles, the nearest vehicle is selected.
- Vehicle location is updated after each successful assignment.
- Orders that cannot be assigned are returned in the unassigned orders list.

**Note:** The application uses a greedy heuristic to assign each order to the nearest vehicle with sufficient remaining capacity. This approach makes locally optimal assignment decisions but does not guarantee a globally optimal dispatch plan.
## Database Configuration

This application uses MySQL to store delivery orders and fleet vehicle information.

## Assumptions

- Order weight and vehicle capacity are measured in kilograms (kg).
- Geographical distances are calculated in kilometers (km).
- HIGH-priority orders are processed before MEDIUM- and LOW-priority orders.
- New orders and vehicles are inserted into the database.
- If an order or vehicle with an existing ID is submitted again, its existing record is updated rather than creating a new record.
- POST requests do not delete records that are not included in the request.
- The dispatch plan is calculated dynamically whenever the GET endpoint is called.
- Generating a dispatch plan does not modify the stored order or vehicle records.
- An order remains unassigned if no available vehicle has sufficient remaining capacity.
- The calculated travel distance does not include a return trip to the vehicle's starting location.

### Database Setup

1. Install and start MySQL Server.
2. Create a database:

```sql
CREATE DATABASE dispatch_load_balancer;
```

3. Update the database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dispatch_load_balancer
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Replace `YOUR_MYSQL_PASSWORD` with your local MySQL password.

The application uses Spring Data JPA and Hibernate to manage database operations and automatically create or update tables.

## Build and Run the Application

### Prerequisites

Make sure the following are installed:

- Java 17 
- Maven.
- MySQL Server.
- IntelliJ IDEA or another Java IDE.

### Steps to Run

1. Clone the repository:

```bash
git clone <your-github-repository-url>
```

2. Navigate to the project directory:

```bash
cd dispatch-load-balancer
```

3. Configure the MySQL database credentials in `application.properties`.

4. Build the project:

```bash
mvn clean install
```

5. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

6. The application will start at:

```text
http://localhost:8086
```

### Access Swagger UI

Open the following URL in your browser to explore and test the REST API endpoints:

http://localhost:8086/swagger-ui/index.html

## Testing

The application includes automated unit tests using JUnit 5 and Mockito.

### Test Coverage

The following scenarios are covered:

- Haversine distance calculation.
- High-priority order assignment.
- Vehicle capacity validation.
- Nearest feasible vehicle selection.
- Empty orders handling.
- Empty vehicles handling.
- Orders exceeding vehicle capacity.
- Multiple orders competing for vehicle capacity.
- Unassigned order handling.

### Execute Tests

Run the following command from the project directory:

```bash
mvn clean test
```

### Test Results

| Metric | Result |
|---|---:|
| Total test cases | 11 |
| Passed | 11 |
| Failed | 0 |
| Errors | 0 |
| Skipped | 0 |

All 11 automated test cases passed successfully.
## Performance Considerations

- Uses Spring Data JPA `saveAll()` to persist multiple orders and vehicles in batches at the repository level.
- Uses in-memory processing to calculate vehicle assignments and delivery distances.
- Sorts orders by priority before assignment.
- Evaluates available vehicles for each order to identify the nearest feasible vehicle.
- Uses database primary keys for order and vehicle identification.

The current greedy assignment algorithm has an approximate time complexity of O(n log n + m log m + n × m), where n represents the number of orders and m represents the number of vehicles.

For larger datasets, performance can be improved through database indexing, batch processing, optimized vehicle selection and more efficient route optimization techniques.

## Future Improvements

- Implement advanced route optimization algorithms such as 2-opt to improve delivery route efficiency.
- Integrate a Maps API to calculate real-world road distances and travel times.
- Add Docker and Docker Compose support for easier application and database setup.
- Implement authentication and authorization to secure API endpoints.
- Introduce pagination for handling large datasets.
- Add integration tests using Spring Boot Test and MockMvc.
- Add code coverage reporting using JaCoCo

## Conclusion

The Dispatch Load Balancer demonstrates a backend solution for assigning delivery orders to fleet vehicles based on priority, vehicle capacity, and geographical distance.

The project applies Java, Spring Boot, REST APIs, MySQL, and automated testing to implement a modular and maintainable dispatch management system.
