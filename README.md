# EduSmartBackend - Setup and Testing Guide

## Prerequisites

- Java 21 installed
- Gradle installed (optional, as you can use the wrapper `./gradlew`)
- MariaDB installed locally (for local testing)
- Environment variables for database credentials

---

## Local Testing Setup

1. Create the test database and user in your local MariaDB:

```sql
CREATE DATABASE testdb;
CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'testpass';
GRANT ALL PRIVILEGES ON testdb.* TO 'testuser'@'localhost';
FLUSH PRIVILEGES;
