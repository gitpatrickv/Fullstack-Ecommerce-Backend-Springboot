FROM openjdk:17-jdk

WORKDIR /app

COPY target/fullstack-backend-springboot-1.0.jar /app/fullstack-backend-springboot.jar

EXPOSE 8080

CMD ["java", "-jar", "fullstack-backend-springboot.jar"]
