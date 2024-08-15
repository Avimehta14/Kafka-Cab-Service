# Kafka-Cab-Service
# Cab Service Location Kafka Project

## Overview

This project simulates a cab service location tracking system using Apache Kafka. It consists of two main components:

1. **Cab Service Driver:** This component simulates the cab driver's location and sends updates to a Kafka topic.
2. **Cab Service User:** This component listens to the Kafka topic for location updates and displays the latest location on a minimalistic UI.

Additionally, the project includes a Dead Letter Queue (DLQ) mechanism to handle and process invalid data. It also supports mechanism of retrying the failed events in the topics upto set 'n' times

## Table of Contents

- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Kafka Topics](#kafka-topics)
- [Components](#components)
  - [Cab Service Driver](#cab-service-driver)
  - [Cab Service User](#cab-service-user)
- [Dead Letter Queue (DLQ)](#dead-letter-queue-dlq)
  - [DLQ Listener Service](#dlq-listener-service)
  - [Simulating Invalid Data](#simulating-invalid-data)
- [Application Properties](#application-properties)

## Technologies Used

- **Java**
- **Spring Boot**
- **Apache Kafka**
- **Thymeleaf (minimal UI)**
- **Gradle**

## Project Structure

- **`cab_service_driver/`**: Contains the driver-related code (e.g., `CabLocationService`, `CabLocationController`).
- **`cab_service_user/`**: Contains the user-related code (e.g., `UserLocationService`, `UserCabLocationController`, `LocationStorage`).
- **`config/`**: Contains Kafka configuration (`KafkaConfig`).
- **`dlq/`**: Contains the DLQ listener service (`DLQListenerService`).

## Kafka Topics

1. **`cab-location`**: The main topic where the cab driver’s location updates are published.
2. **`cab-location-dlq`**: The Dead Letter Queue (DLQ) topic where invalid location messages are routed.

## Components

### Cab Service Driver

- **CabLocationService.java**: 
  - Simulates and sends the cab driver's location updates to the `cab-location` Kafka topic.
  - Includes a method to simulate random location updates, with a 20% chance of sending invalid data.
  
- **CabLocationController.java**:
  - REST controller to trigger the simulation of location updates. Accessible via a PUT request.

### Cab Service User

- **UserLocationService.java**:
  - Listens to the `cab-location` topic for location updates.
  - Validates the received location data and stores it in `LocationStorage`.
  - Sends invalid data to the `cab-location-dlq` topic.
  
- **UserCabLocationController.java**:
  - Handles requests to display the latest cab location.
  - Uses Thymeleaf to render the location on a minimalistic UI.
  
- **LocationStorage.java**:
  - Stores the latest valid cab location to be displayed to the user.

## Dead Letter Queue (DLQ)

### DLQ Listener Service

- **DLQListenerService.java**:
  - Listens to the `cab-location-dlq` topic.
  - Logs the invalid location messages for further analysis or processing.

### Simulating Invalid Data

- The `CabLocationService` has a built-in mechanism to randomly generate invalid location data (e.g., `"INVALID-DATA"`).
- These invalid messages are routed to the `cab-location-dlq` topic, where they are processed by the `DLQListenerService`.

## Application Properties

Group IDs for Kafka consumers are defined in the `application.properties` file:

```properties
# Group ID for the main consumer (UserLocationService)
kafka.consumer.group-id.user=user-group

# Group ID for the DLQ listener (DLQListenerService)
kafka.consumer.group-id.dlq=dlq-group
```

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements, bug fixes, or new features.
