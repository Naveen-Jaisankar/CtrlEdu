# Ctrl+Edu - Distributed Educational Campus Management System

## Team Group Name: Jobizzz
### Team Members:
- Naveen Jaisankar (23200145)
- Shaline Raghupathy (23201217)
- Nithishh Saravanan (23200138)

## Project Overview
Ctrl+Edu is a comprehensive distributed educational campus management system designed to facilitate efficient campus operations for students, lecturers, and super admins. The system addresses critical distributed system challenges, including fault tolerance, scalability, and real-time communication, with the following key features:

- **Role-Based Access Control:** Secure access for students, lecturers, and super admins using Keycloak.
- **Course & Enrollment Management:** Super admins can manage courses, enroll students, and assign lecturers.
- **Real-Time Chat Service:** WebSockets and Kafka power real-time chat communication for subject-specific collaboration.
- **Scalability & Fault Tolerance:** Technologies like Redis, Kafka, RabbitMQ, and Eureka ensure high availability.
- **Notification Service:** RabbitMQ-based notification service sends automated emails for critical events.
- **Gateway Service:** Netflix Eureka for service discovery and API routing.

## Technology Stack

### Frontend:
- **React with TypeScript**
- **Vite**
- **Tailwind CSS**
- **WebSocket Integration**
- **Keycloak JavaScript Adapter**

### Backend (Microservices):
- **Java with Spring Boot**
- **AuthService:** Manages authentication using Keycloak.
- **ChatService:** Real-time messaging using WebSockets and Kafka.
- **MasterService:** Core business logic and database management.
- **NotificationService:** RabbitMQ-based email notifications.
- **GatewayService:** Spring Cloud Gateway for routing and token validation.
- **EurekaService:** Service discovery and load balancing with Netflix Eureka.

### Infrastructure & Tools:
- **Kafka & Zookeeper** (Message Streaming)
- **Redis** (Caching)
- **RabbitMQ** (Message Queue)
- **PostgreSQL** (Relational Database)
- **Docker & Docker Compose**
- **Netflix Eureka** (Service Discovery)
- **Keycloak** (Identity and Access Management)

---

## How to Compile and Run the Code

### Prerequisites:
- Docker and Docker Compose
- Java 17+
- Node.js with npm
- Keycloak setup (Configured Realm and Client)

### Steps:
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Naveen-Jaisankar/CtrlEdu.git
   cd CtrlEdu
   ```
2. **Start the Services:**
   ```bash
   docker-compose up --build
   ```
3. **Keycloak Configuration:**
   - Access Keycloak URL: `http://localhost:8080`
   - Login with `admin` as both username and password.
   - Create a new realm named `CtrlEdu`.
   - Create a client ID named `ctrledu-client`.
   - Enable:
     - Client authentication
     - Client authorization
     - Standard Flow
     - Direct Access Grants
     - Service Account Roles
     - OAuth 2.0 Device Authorization Grant
   - Set:
     - Root URL: `http://localhost:8080/`
     - Valid Redirect URI: `http://localhost:8080/*`
     - Web Origin: `http://localhost:8080/`
   - Click Save.
   - Create realm roles: `student`, `super-admin`, `teacher` (all case-sensitive).
   - Go to Clients > ctrledu-client > Credentials tab and copy the Client Secret.
   - Paste the client secret in the `application.properties` of Auth Service for the key `keycloak.credentials.secret`.

4. **Restart Services:**
   ```bash
   docker-compose down
   # Delete all Docker images and containers but not volumes
   mvn clean install
   docker-compose up --build
   ```

5. **Run Frontend Manually (Optional):**
   ```bash
   cd Frontend/Client
   npm install
   npm run dev
   ```
6. **Access the Application:**
   - Frontend: `http://localhost:3000`
   - Keycloak Admin: `http://localhost:8080`

7. **Stopping Services:**
   ```bash
   docker-compose down
   ```

---

## Project Structure
- `frontend/`: React frontend with TypeScript and Vite
- `auth-service/`: Handles authentication using Keycloak
- `chat-service/`: Real-time chat service using Kafka and WebSockets
- `master-service/`: Core academic management logic
- `notification-service/`: RabbitMQ-driven notification handling
- `gateway-service/`: Central API gateway
- `eureka-service/`: Service discovery using Netflix Eureka
- `docker-compose.yml`: Manages all services and dependencies

---

## Links:
- [Project Report](./CtrlEdu Report.pdf)
- [Demo Video](CtrlEdu_Demo.mp4)

---

## Contributions
- **Naveen Jaisankar:** CommonService, EurekaService, GatewayService, NotificationService, Docker setup.
- **Shaline Raghupathy:** ChatService, MasterService, WebSocket Integration, Redis Caching.
- **Nithishh Saravanan:** AuthService, Keycloak Integration, Frontend Dashboard (Admin, Teacher, Student).

---

## License
This project is licensed under the MIT License.

