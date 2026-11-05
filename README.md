# CloudOps Monitor

### AWS-Based Linux Server Deployment, Monitoring & Troubleshooting Lab

CloudOps Monitor is a full-stack server monitoring application deployed on AWS EC2.

It collects server performance metrics, displays them through a web dashboard, and stores historical monitoring data in an Oracle Database.

## Technology Stack

- AWS EC2
- Amazon Linux 2023
- Apache HTTP Server
- Java 25
- Spring Boot 4.0.0
- HTML5, CSS3 and JavaScript
- Oracle Database
- Spring JDBC
- AWS CloudWatch
- AWS S3 and IAM
- Maven and Git

## Key Features

- Live CPU utilization monitoring
- Memory usage monitoring
- Disk usage monitoring
- AWS CloudWatch metric integration
- Automatic metric collection every 60 seconds
- Historical metrics stored in Oracle Database
- REST APIs built with Spring Boot
- Interactive web dashboard
- Apache reverse proxy configuration
- AWS S3 backup integration
- IAM-based access management
- Application health monitoring
- Centralized exception handling
- Basic Apache security hardening

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/metrics` | Fetch current server metrics |
| GET | `/api/metrics/history?limit=50` | Fetch historical metrics |
| GET | `/actuator/health` | Check application health |

## Architecture

1. A user opens the monitoring dashboard in a web browser.
2. Apache HTTP Server serves the frontend.
3. Apache forwards API requests to the Spring Boot backend.
4. Spring Boot retrieves infrastructure metrics from AWS CloudWatch.
5. The backend stores historical metrics in Oracle Database.
6. AWS S3 is used for private backup storage.
7. AWS IAM manages permissions for AWS services.

### Monitoring Flow

Browser → Apache → Spring Boot REST API → AWS CloudWatch

Spring Boot REST API → Oracle Database (Historical Metrics)

AWS EC2 → AWS S3 (Backup Storage)

## Deployment Requirements

- AWS EC2 instance running Amazon Linux 2023
- Java 25 and Maven
- Apache HTTP Server
- AWS IAM permissions for CloudWatch and S3
- Oracle Database with the required schema
- Network connectivity between the backend and Oracle Database

### Database Configuration

Database credentials are supplied through environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Credentials are not stored in the source code.

### Lab Environment Note

In this lab deployment, the Oracle Database runs on a local Windows machine and is accessed from EC2 through a reverse SSH tunnel.

The Windows machine, Oracle Database service, and SSH tunnel must remain available for database connectivity.

## Build Instructions

From the project root, navigate to the backend:

`cd backend/cloudops-api`

Build the Spring Boot application:

`mvn clean package -DskipTests`

The generated JAR file will be available in the target directory.

## Project Purpose

This project demonstrates practical skills in AWS cloud operations, Linux server administration, application deployment, infrastructure monitoring, REST API development, and database integration.
