# ✅ Build stage — Maven se JAR banao
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -q

# ✅ Run stage — sirf JRE use karo (Maven nahi)
# eclipse-temurin:17-jre = 200MB kam RAM use karta hai
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/interview-prep-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", \
     "-Xms48m", \
     "-Xmx148m", \
     "-XX:+UseSerialGC", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", "app.jar"]