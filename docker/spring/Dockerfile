# образ Alpine Linux
FROM openjdk:19-jdk-alpine

WORKDIR /app

COPY coworking-test-project-0.0.1-SNAPSHOT.jar app.jar
COPY start.sh .
RUN chmod +x ./start.sh

EXPOSE 9999:8080

CMD ["./start.sh"]


