# S4.02 - API REST with Springboot Framework & H2

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

-----

## Summary
In this task you will do a **CRUD** (Create, Read, Update, Delete) 
with 3 different databases.

You will learn how to correctly use HTTP verbs and how 
to manage response codes.

-----

## 📄 LVL 1: Spring & H2
### Key Components

* We have an **Entity** called "Fruit", which has the following 
properties:
  * int id
  * String name
  * int quantityKilos

* **H2 Database**
* Subpackages:
  * **Controllers**
  * **Model**
  * **Services**
  * **Repository**
  * **Exception**

-----

## 🔧 Technologies
- **Java 24**: Core programming language
- **Maven**: Build and dependency management
- **H2**: In-memory Database
- **Git/GitHub**: version control
- **Additional dependencies**:
  - JDBC
  - Postman

-----

### 🛠️ SCRIPTS
The following commands can be copied and executed directly in the 
project's root directory.

#### Compile Project
```bash
mvn compile
```
#### Package to .jar
```bash
mvn package
```
#### Clean Project
```bash
mvn clean
```
#### Execute the Application
```bash
mvn spring-boot:run
```
---

### 🍎 API ENDPOINTS REFERENCE
The base URL for the API is http://localhost:8080/api/v1/fruits

| Operation | HTTP Method | Endpoint | Description | Status Codes |
|------------|--------------|-----------|--------------|---------------|
| **Create** | POST | `/api/v1/fruits` | Creates a new fruit. | `201 Created`, `400 Bad Request` |
| **Read One** | GET | `/api/v1/fruits/{id}` | Retrieves a single fruit by ID. | `200 OK`, `404 Not Found` |
| **Read All** | GET | `/api/v1/fruits` | Retrieves a paginated list of all fruits. | `200 OK` |
| **Update** | PUT | `/api/v1/fruits/{id}` | Updates an existing fruit. | `200 OK`, `404 Not Found` |
| **Delete** | DELETE | `/api/v1/fruits/{id}` | Deletes a fruit by ID. | `204 No Content`, `404 Not Found` |
---

### 🛠️  HTTP Requests (cURL Examples)
#### CREATE Fruit (POST)
```bash
curl -X POST http://localhost:8080/api/v1/fruits \
-H "Content-Type: application/json" \
-d '{"name": "Pineapple", "quantityKilos": 25}'
```
#### READ All Fruits (GET)
```bash
# Retrieve the first page of fruits (default size: 20)
curl http://localhost:8080/api/v1/fruits
```
#### READ One Fruit (GET)
```bash
# Replace {id} with an actual Fruit ID (e.g., 1)
curl http://localhost:8080/api/v1/fruits/1
```
#### UPDATE Fruit (PUT)
```bash
# Replace {id} with an actual Fruit ID (e.g., 1)
curl -X PUT http://localhost:8080/api/v1/fruits/1 \
-H "Content-Type: application/json" \
-d '{"name": "Strawberry", "quantityKilos": 12}'
```
#### DELETE Fruit (DELETE)
```bash
# Replace {id} with an actual Fruit ID (e.g., 1)
curl -X DELETE http://localhost:8080/api/v1/fruits/1
```
-----

## 📚 Additional Resources
- [Badges](https://github.com/alexandresanlim/Badges4-README.md-Profile?tab=readme-ov-file#-frameworks--library-)
- [Springboot](https://dev.to/abhi9720/a-beginners-guide-to-crud-operations-of-rest-api-in-spring-boot-mysql-5hcl)
- [Postman](https://learning.postman.com/docs/getting-started/first-steps/sending-the-first-request/)
- [H2](https://www.h2database.com/html/tutorial.html#connecting_using_jdbc)
- [Hibernate](https://www.baeldung.com/spring-boot-hibernate)
- [HTTP](https://www.restapitutorial.com/httpstatuscodes)

[Back to top](#top)
