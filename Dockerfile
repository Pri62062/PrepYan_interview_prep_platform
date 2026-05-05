FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -q

CMD ["java", \
     "-Xms32m", \
     "-Xmx160m", \
     "-XX:+UseSerialGC", \
     "-XX:MaxMetaspaceSize=60m", \
     "-XX:TieredStopAtLevel=1", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", "target/interview-prep-0.0.1-SNAPSHOT.jar"]