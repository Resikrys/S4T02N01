# S4.02 - API REST with Springboot Framework & H2

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

-----

## Summary
In this task you will do a CRUD (Create, Read, Update, Delete) 
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
```bash
mvn compile          # Compile project
mvn package          # Package in .jar
mvn clean            # Clean the project
mvn spring-boot:run  # execute the app
```

### 🛠️ HTTP requests to update and consult information (POSTMAN)
```bash
URL base:
http://localhost:8080/fruits-rest

(POST):
http://localhost:8080/fruits-rest + raw JSON:
{
  "name": "Pineapple",
  "quantityKilos": 25
}

(GET):
http://localhost:8080/fruits-rest/{id}
http://localhost:8080/fruits-rest/getAll

(PUT):
http://localhost:8080/fruits-rest/{id}

(DELETE):
http://localhost:8080/fruits-rest/{id}

```
### 🛠️ HTTP requests to update and consult information (CURL)
```bash
(GET):
curl http://localhost:8080/fruits-rest
curl http://localhost:8080/fruits-rest/1

(POST):
curl -X POST http://localhost:8080/fruits-rest -H "Content-Type: application/json" -d '{"name": "Pineapple", "quantityKilos": 25}'

(PUT):
curl -X PUT http://localhost:8080/fruits-rest/1 -H "Content-Type: application/json" -d '{"name": "Strawberry", "quantityKilos": 12}'


(DELETE):
curl -X DELETE http://localhost:8080/fruits-rest/1

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
