<h3>APIS FOR HHub SYSTEM</h3>

---

<h3>Abstract </h3>

Inspired by an idea about a large-scale social networking platform that support vast user base to communicate. Also, it's a chance for me to cultivate my mindset needed to architect and manage high-traffic applications that serve a large number of users. It's why this project was conceived, to help me explore more about scalable architectures, performance optimization, and deployment workflows for real-world social networking systems.

<div align="center" >
<img src="./docs/asserts/architect01.png" style="max-height:400px">
</div>

| Service              | Main Tech Stack                   | Description                                                                              |
| -------------------- | --------------------------------- | ---------------------------------------------------------------------------------------- |
| API Gateway          | Java, Spring Boot                 | Single entry point for all request from client, routes and protects these service behind |
| Identity Provider    | (Java, Spring Authorization Server)/(Keycloak) | Manages user identities, handles user authentication and authorization                   |
| Profile Service      | Python with Django                | Resource server for user profile features                                                |
| Blog Service         | ASP.NET Core 8 Web API            | Resource server for post features                                                        |
| Connection Service   | Go with Gin                       | Resource server for user connection features                                             |
| Notification Service | Node.js Express                   | Resource server for notification features                                                |

----


<h3>Technical Overview</h3>


**Identity Provider**: 

At first, I've chosen **Keycloak** as a part of my social system  as **Single Sign-On (SSO)** server. For the frontend, I registered a external client that supported authorization mechanism by **Authorization Code Grant** (**PKCE**). And the internal client, for these services (also gateway) was set up as **Authorization Resource Server**. Early on, I just issued **JWT** token format (rather than **opaque token**) for *access token* and *refresh token*, so the *gateway* and *downstream services* simply fetch from `/.well-know` endpoint to verify *access token*.

To gain finer control over user workflows and registration, I  plan to migrate from **Keycloak** to **Spring Authorization Server** (v1.4.x). Due to the mature of Java/Spring/Spring Security, it has a rich ecosystem that will allow me to build a fully **customized OAuth2** server that seamlessly supports SSO, and security considering. Although, it need more time for me to dive deep into this interested concepts. In soon, the customize authorization server that support SSO can migrate to the system.


**API Gateway**:

Build base on **Spring Cloud Gateway** (reactive programming paradigm). It serves as the single entry point for all clients, routing and load-balancing requests to downstream services. On behalf of its protected services to play a role as **OAuth2 Client** before external request to secured resource and be a **Resource Server** to validate the attached access token from request header.

To improve resilience, we’ve integrated Resilience4J for rate limiting and circuit breaking, ensuring that transient failures or traffic spikes don’t cascade across the system.


**Blog Service**:

Build base on **.NET Core Web API**, its feature relate to how user post a new and provide interested feed page for them. Post and commend records has stored **Mongo DB** by MongoDB Driver. Future work includes refactoring the codebase toward a **Hexagonal Architecture** also extend entity model to handle more complex personal user's feed.


**Profile Service**:

Implemented using **Django REST Framework** and managed with **Poetry**, the Profile Service stores user profiles in **PostgreSQL**. Its modular project structure makes it easy to extend and maintain. Currently, I rely on Django’s built-in ORM for migration schema.


**Notification Service**:

Build base on **NodeJS Express**, consume **Kafka** notification topic and store message to **Mongo DB**, support for user to get their notification. Exposing endpoints for users to retrieve their notifications is the first approach I chosen. I'll soon add push-notification support and more advanced delivery guarantees.


**Connection Service**:

Is written in **Go** using the **Gin** framework. It manages user relationships—friendships and follows—and persists data in MySQL via the **GORM ORM**. The project follows a standard Go project layout, with a few customizations for our domain logic.

Move ahead, I’ll integrate **Redis** to maintain high-availability connection state and introduce real-time friend-request notifications via **Kafka**, which will feed into Notification Service.


**HH Community UI**

The UI is developed in **ReactJS**, incorporating key libraries: **MUI** for component styling, **Styled Components** for custom theming, **Axios** for HTTP requests, **Redux** for state management, and **React Router DOM** for client-side navigation. Perhaps I can cover fullstack, but I recognize that the UI side isn’t my absolute strongest suit. This project still have more improvement in the coming updates.










