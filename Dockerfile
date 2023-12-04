FROM openjdk:18.0.2.1

# Set the working directory to /app
WORKDIR /app

# Copy the shadow JAR file into the container
COPY server/build/libs/server-all.jar .

EXPOSE 8081:8081

# Specify the default command to run your service
CMD ["java", "-jar", "server-all.jar"]