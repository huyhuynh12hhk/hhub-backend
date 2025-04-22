<h3>APIS FOR HHub SYSTEM</h3>

<h4>Abstract </h4>
<div align="center" >
<img src="./docs/asserts/architect01.png" style="max-height:400px">
</div>

| Service              | Main Tech Stack        | Description                                                                              |
| -------------------- | ---------------------- | ---------------------------------------------------------------------------------------- |
| API Gateway          | Java, Spring Boot      | Single entry point for all request from client, routes and protects these service behind |
| Identity Provider    | Key Cloak              | Manages user identities, handles user authentication and authorization                   |
| Profile Service      | Python with Django     | Resource server for user profile features                                                |
| Blog Service         | ASP.NET Core 8 Web API | Resource server for post features                                                        |
| Connection Service   | Go with Gin            | Resource server for user connection features                                             |
| Notification Service | Node.js Express        | Resource server for notification features                                                |
