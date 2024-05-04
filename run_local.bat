@echo off
rem Set environment variables for database connection
set DB_HOSTNAME=localhost
set DB_PORT=3306
set DB_NAME=adventurerslist
set DB_USER=adventurer
set DB_PASS=very_secure_password

rem Start MySQL container
echo Starting MySQL container...
docker-compose up -d adventurerslist-db

rem Run Spring Boot application using Maven
echo Running Spring Boot application...
mvn spring-boot:run
