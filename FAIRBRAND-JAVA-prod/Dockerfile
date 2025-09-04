# Use an official Tomcat image as a base image
FROM tomcat:10.1.36-jdk21-temurin-noble

# Set the working directory inside the container
WORKDIR /usr/local/tomcat/webapps

# Copy the WAR file into the Tomcat webapps directory
COPY coltip-backend-mainapp/target/coltip.war /usr/local/tomcat/webapps/coltip.war

# Expose the Tomcat default port
EXPOSE 8080

# Start Tomcat in the foreground
CMD ["catalina.sh", "run"]