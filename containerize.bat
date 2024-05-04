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

rem Build Maven project
echo Building Maven project and starting Spring Boot server...
mvn package && docker-compose up spring-server
