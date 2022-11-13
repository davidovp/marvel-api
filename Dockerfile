FROM eclipse-temurin:11
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# app port
EXPOSE 8080/tcp
# management port
EXPOSE 9090/tcp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]