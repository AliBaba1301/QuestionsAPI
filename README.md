# Questions API

## Instructions for Running Application
This Application uses Java 17 with Spring Boot
1. Set Java version to Java 17
2. Open in IDE (Steps for IntelliJ)
3. `./gradlew clean build` to build the project
4. Create a new Spring Boot Configuration with the below Specifications
   ![f08e7f5067044784f42d0c7c8e4ea4093cd7c19832342ef52a1c115c658e2c56](https://github.com/user-attachments/assets/50659a02-4083-47c0-a75a-ff14271bdb7c)
5. Press the play button in the top corner
- NOTE: This API utilises an In Memory DB that closes when the application stops running.

In order to access the DB start the application and go to: http://localhost:8080/h2-console/
- Username: `sa`
- Password: `password`

### Exercising the API
The attached postman collection can be imported and used in order to verify the usability of the API.
Documentation for the api can be found at once application is running: http://localhost:8080/docs

## Notes during implementation
Due to time constraints I only implemented a few unit tests to ensure base functionality.
Additionally, as this is my first time working with an in memory database the implementation potentially could be improved.
