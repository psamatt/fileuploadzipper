### File Upload Zipper application

Spring Boot app build using Gradle with Docker / Docker compose, API documented using Swagger. 

Postman collection can be found at ./postman_collection.json

#### Gradle
Build Jar
```shell
./gradlew clean assemble
```

Execute tests
```shell
./gradlew test
```

Run locally
```shell
./gradlew bootrun
```

#### Docker Compose
Run Docker image
```shell
docker-compose up -d --build 
```

#### Docker
Build Docker image
```shell
docker build -f Dockerfile -t psamatt/fileuploadzipper .
```

Run Docker image (without compose)
```shell
docker run -d -p 8080:8080 psamatt/fileuploadzipper
```

View Docker logs
```shell
docker logs -f <container_id>
```

#### Swagger

Open Browser, visit: http://localhost:8080/swagger-ui.html

#### Postman

Import the postman collection and try it out

./postman_collection.json