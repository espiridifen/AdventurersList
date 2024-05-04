CREATE DATABASE IF NOT EXISTS adventurerslist;
CREATE USER 'adventurer'@'%' IDENTIFIED BY 'very_secure_password';
GRANT ALL PRIVILEGES ON adventurerslist.* TO 'adventurer'@'%';
FLUSH PRIVILEGES;
