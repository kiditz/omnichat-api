FROM openjdk:11.0.9-jdk
COPY build/libs/app.jar /app/app.jar
CMD ["java", "-Djava.securirt.egd=file:/dev/.urandom", "-jar", "/app/app.jar"]