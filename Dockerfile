FROM openjdk:11.0.9-jdk
COPY build/libs/api-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-Djava.securirt.egd=file:/dev/.urandom", "-jar", "/app/app.jar"]