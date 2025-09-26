# Weather Metrics API

A RESTful API to receive and query weather sensor metrics (e.g., temperature, humidity) from various sensors.
---

##  Features
- REST Endpoint for queries weather metrics by:
- http://{HOST_URL}/api/weather/metrics
    - Sensor ID(s)
    - Metric type(s)
    - Statistics
    - Date range (from 1 day up to 1 month)


### Prerequisites

- Java 17+
- Maven 3+
- 
## Tech Stack

- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- H2 In-Memory Database
- Lombok
- JUnit 5

---


### Implementation explanation

- Standard REST endpoint (/api/weather/metrics) that will return weather metrics according to specification via weatherMetricController and WeatherMetricService
- Sensor data reading is currently using by dummy static data generation in class MetricsCollectorService (we need real implementation here)
- Strategy pattern are used to read Sensor data reading Interface and subclass in MetricsCollectorService
- Input validator has implemented in RequestValidatorUtils
- Controller advice are used to handle exception and return to client
- Unit test and Integration tests are written only few. Majority test cases are missing
- Database access done by JpaRepository. Instead of H2, in future we can use real database like PostgresSQL
- Documentation, code comments is totally missing, didn't get enough time

### Future / production implementation

-- We will talk about microservice separation in sensor-data-reader and weather-metric-query.
-- We can make sure high scalability Load Balancing, Caching, Microservices Architecture, Database Optimization
Use indexing, replication,
-- Unit test/integration test more coverage all corner cases
-- write API load test
-- documentation / comments / method explanation

### Run the Application

```bash
git clone https://github.com/your-username/wmg.git
cd mvn
mvn clean install
mvn spring-boot:run
