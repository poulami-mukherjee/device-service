# Device Service

This project is a Spring Boot application that provides a REST API for managing a device database. It supports operations such as adding, retrieving, updating, deleting, and searching for devices.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Database](#database)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Testing](#testing-the-endpoints)
- [Troubleshooting](#trobleshooting)

## Features

- Add a new device
- Retrieve a device by ID
- Retrieve all devices
- Search devices by brand
- Update a device
- Partially update a device
- Delete a device

## Prerequisites

- Java 17 or later
- Apache Maven 3.6.3 or later
- (Optional) Postman for testing API endpoints

## Getting Started

1. **Clone the repository:**

    ```sh
    git clone https://github.com/your-username/device-management-service.git
    cd device-management-service
    ```

2. **Configure the application:**

   Ensure your `application.properties` file is configured correctly for H2 in-memory database:

    ```properties
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    ```
The application uses an H2 in-memory database for development and testing. You can configure the database settings in the `src/main/resources/application.properties` file.

3. **Build the project:**

    ```sh
    mvn clean install
    ```


4. **Run the application:**

    ```sh
    mvn spring-boot:run
    ```

## Database

### Database Choice

For this project, we use the H2 in-memory database. This choice was made for several reasons:
- **Development Convenience:** H2 is lightweight and requires no additional setup, making it ideal for development and testing.
- **In-Memory Operations:** The in-memory nature of H2 means that the database is fast and resets each time the application restarts, ensuring a clean slate for each test run.
- **Embedded Console:** H2 provides a web-based console for querying and managing the database, which can be very useful for development.

### H2 Console Access

If you need to check the database contents, you can access the H2 console:

1. Open your browser and navigate to `http://localhost:8080/h2-console`.
2. Use the following settings to connect:
    - **JDBC URL:** `jdbc:h2:mem:testdb`
    - **Username:** `sa`
    - **Password:** (leave blank)
3. Click "Connect" to log in and query the database.

## Project Structure

The project follows a typical Spring Boot structure:

```css
cssCopy code
device-management-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── devicemanagementservice/
│   │   │               ├── controller/
│   │   │               │   └── DeviceController.java
│   │   │               ├── dto/
│   │   │               │   └── DeviceDto.java
│   │   │               ├── exception/
│   │   │               │   ├── DeviceNotFoundException.java
│   │   │               │   └── DeviceServiceException.java
│   │   │               ├── model/
│   │   │               │   └── Device.java
│   │   │               ├── repository/
│   │   │               │   └── DeviceRepository.java
│   │   │               └── service/
│   │   │                   └── DeviceService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── devicemanagementservice/
│                       └── DeviceServiceTests.java
├── .gitignore
├── pom.xml
└── README.md

```

- **controller:** Contains the REST controllers.
- **dto:** Contains Data Transfer Objects.
- **exception:** Contains custom exception classes.
- **model:** Contains JPA entity classes.
- **repository:** Contains Spring Data JPA repositories.
- **service:** Contains service classes with business logic.

## API Endpoints

### Add a Device

- **URL:** `/devices/create`
- **Method:** POST
- **Headers:**
    - `Content-Type: application/json`
  - **Body:**
    ```json
    {
      "name": "Device1",
      "brand": "BrandA"
    }
    ```
  - **Response:**
    - **Status:** 201 Created
    - **Body:**
     ```json
     {
       "id": 1,
       "name": "Device1",
       "brand": "BrandA",
       "creationTime": "2024-07-01T01:13:01.722+02:00"
     }
- **Exception:** 
  - DeviceServiceException (500 HTTP Status Code) if an error occurs while creating the device

### Retrieve a Device by ID
- **URL:** `/devices/{id}`
- **Method:** GET
- **Response:**
  - **Status:** 200 OK
  - **Body:**
  ```json
    {
       "id": 1,
       "name": "Device1",
       "brand": "BrandA",
       "creationTime": "2024-07-01T01:13:01.722+02:00"
    }
- **Exception:** 
  - DeviceNotFoundException (404 HTTP Status Code) if the device with the specified ID is not found
  - DeviceServiceException (500 HTTP Status Code) if an error occurs while retrieving the device

### Retrieve All Devices
- **URL:** `/devices/`
- **Method:** GET
- **Response:**
    - **Status:** 200 OK
    - **Body:**
  ```json
  [
     {
        "id": 1,
        "name": "Device1",
        "brand": "BrandA",
        "creationTime": "2024-07-01T01:13:01.722+02:00"
     },
     {
         "id": 2,
         "name": "Device2",
         "brand": "BrandB",
         "creationTime": "2024-07-01T01:15:01.722+02:00"
     }
  ]


- **Exception:**
    - DeviceServiceException (500 HTTP Status Code) if an error occurs while retrieving the device

### Search Devices by Brand
- **URL:** `/devices/brand/{brand}`
- **Method:** GET
- **Response:**
    - **Status:** 200 OK
    - **Body:**
  ```json
  [
     {
        "id": 1,
        "name": "Device1",
        "brand": "BrandA",
        "creationTime": "2024-07-01T01:13:01.722+02:00"
     }
  ]

- **Exception:**
    - DeviceServiceException (500 HTTP Status Code) if an error occurs while retrieving the device

### Update a device
- **URL:** `/devices/{id}`
- **Method:** PUT
- **Headers:**
  - **Content-Type:** application/json
  -  - **Body:**
  ```json
    {
       "name": "UpdatedDevice",
       "brand": "UpdatedBrand"
    }

- **Response:**
    - **Status:** 200 OK
    - **Body:**
  ```json
  {
      "id": 1,
      "name": "UpdatedDevice",
      "brand": "UpdatedBrand",
      "creationTime": "2024-07-01T01:13:01.722+02:00"
  }

- **Exception:**
  - DeviceNotFoundException (404 HTTP Status Code) if the device with the specified ID is not found
  - DeviceServiceException (500 HTTP Status Code) if an error occurs while updating the device

### Partially update a device

- **URL:** `/devices/{id}`
- **Method:** PATCH
- **Headers:**
    - **Content-Type:** application/json
    -  - **Body:**
  ```json
    {
       "name": "PartiallyUpdatedDevice"
    }

- **Response:**
    - **Status:** 200 OK
    - **Body:**
  ```json
  {
      "id": 1,
      "name": "PartiallyUpdatedDevice",
      "brand": "UpdatedBrand",
      "creationTime": "2024-07-01T01:13:01.722+02:00"
  }

- **Exception:**
    - DeviceNotFoundException (404 HTTP Status Code) if the device with the specified ID is not found
    - DeviceServiceException (500 HTTP Status Code) if an error occurs while updating the device


### Delete a device
- **URL:** `/devices/{id}`
- **Method:** DELETE
- **Response:**
   - **Status:** 204 No Content
- **Exception:**
    - DeviceNotFoundException (404 HTTP Status Code) if the device with the specified ID is not found
    - DeviceServiceException (500 HTTP Status Code) if an error occurs while updating the device

    
## Testing the Endpoints

You can test the API endpoints using Postman or cURL.

#### Using Postman

1. **Add a Device:**
    - **Method:** POST
    - **URL:** `http://localhost:8080/devices/create`
    - **Headers:**
        - `Content-Type: application/json`
    - **Body:**

        ```json
        {
          "name": "Device1",
          "brand": "BrandA"
        }
        
        ```

2. **Retrieve a Device by ID:**
    - **Method:** GET
    - **URL:** `http://localhost:8080/devices/1`
3. **Retrieve All Devices:**
    - **Method:** GET
    - **URL:** `http://localhost:8080/devices`
4. **Update a Device:**
    - **Method:** PUT
    - **URL:** `http://localhost:8080/devices/1`
    - **Headers:**
        - `Content-Type: application/json`
    - **Body:**

        ```json
        {
          "name": "UpdatedDevice",
          "brand": "UpdatedBrand"
        }
        
        ```

5. **Partially Update a Device:**
    - **Method:** PATCH
    - **URL:** `http://localhost:8080/devices/1`
    - **Headers:**
        - `Content-Type: application/json`
    - **Body:**

        ```json
        {
          "name": "PartiallyUpdatedDevice"
        }
        
        ```

6. **Delete a Device:**
    - **Method:** DELETE
    - **URL:** `http://localhost:8080/devices/1`
7. **Search Devices by Brand:**
    - **Method:** GET
    - **URL:** `http://localhost:8080/devices/brand/BrandA`

#### Using cURL

1. **Add a Device:**

    ```
    curl -X POST http://localhost:8080/devices/create \
    -H "Content-Type: application/json" \
    -d '{"name": "Device1", "brand": "BrandA"}'
    
    ```

2. **Retrieve a Device by ID:**

    ```
    curl -X GET http://localhost:8080/devices/1
    
    ```

3. **Retrieve All Devices:**

    ```
    curl -X GET http://localhost:8080/devices
    
    ```

4. **Update a Device:**

    ```
    curl -X PUT http://localhost:8080/devices/1 \
    -H "Content-Type: application/json" \
    -d '{"name": "UpdatedDevice", "brand": "UpdatedBrand"}'
    
    ```

5. **Partially Update a Device:**

    ```
    curl -X PATCH http://localhost:8080/devices/1 \
    -H "Content-Type: application/json" \
    -d '{"name": "PartiallyUpdatedDevice"}'
    
    ```

6. **Delete a Device:**

    ```
    curl -X DELETE http://localhost:8080/devices/1
    
    ```

7. **Search Devices by Brand:**

    ```
    curl -X GET http://localhost:8080/devices/brand/BrandA
    
    ```
## Trobleshooting
If you encounter any issues:

1. **Check Application Logs:**
    - Review the logs in your console or terminal where the application is running for any errors or warnings.
2. **Verify Configuration:**
    - Ensure the `application.properties` file is correctly configured for the H2 database.
3. **Database URL:**
    - Ensure you are using the correct JDBC URL (`jdbc:h2:mem:testdb`) for the in-memory database.
4. **Dependency Issues:**
    - Ensure all required dependencies are correctly specified in the `pom.xml` file.
