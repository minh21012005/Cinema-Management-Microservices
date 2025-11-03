# ---- Run Stage ----
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy file jar từ thư mục target
COPY target/*.jar app.jar

EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]