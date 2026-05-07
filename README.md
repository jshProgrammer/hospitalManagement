<p align="center">
  <h1 align="center">Hospital Management System</h1>
</p>

<p align="center">
  A fullstack hospital management platform focused on secure handling of sensitive medical data, scalable backend architecture, and optimized database operations.
</p>

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Security & Data Protection](#security--data-protection)
- [Performance Optimization](#performance-optimization)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Roadmap](#project-roadmap)
- [Team](#team)
- [License](#license)

---

# Overview

Hospital Management System is a fullstack software project designed to manage hospital-related workflows including patients, doctors, nurses, departments, rooms, bookings, diagnoses, and medication management.

The project focuses especially on:

- secure storage of sensitive personal data
- database optimization and query performance
- scalable backend architecture
- maintainable REST API design
- modular frontend development

The backend is implemented using Kotlin, Spring Boot, Hibernate, and PostgreSQL, while the frontend is built with React and Axios.

A key aspect of the project is the protection of personal and medical data through encryption mechanisms and optimized database access patterns.

---

# Features

## Core Features

- Patient management
- Doctor management
- Nurse management
- Department management
- Station and room management
- Booking management
- Medication management
- Diagnosis management

## Backend Features

- RESTful API architecture
- Layered backend structure
- JPA/Hibernate ORM integration
- PostgreSQL database integration
- Dynamic filtering using JPA Specifications
- Modular service architecture
- Repository abstraction layer
- DTO/request model separation

## Frontend Features

- React-based frontend
- Axios API integration
- Modular component architecture
- Reusable UI components
- Dynamic data tables
- Entity-based navigation system

## Security Features

- Encryption of sensitive personal data
- Secure handling of medical information
- Separation of business logic and persistence layer

---

# Architecture

The project follows a layered architecture approach:

```text
Frontend (React)
        ↓
REST API Controllers
        ↓
Service Layer
        ↓
Repository Layer
        ↓
PostgreSQL Database
```

## Backend Structure

- `api/`
  - REST controllers and request models
- `service/`
  - Business logic layer
- `dbRepositories/`
  - Database access and JPA Specifications
- `models/`
  - Domain entities and enums

## Frontend Structure

- `components/`
  - Reusable UI components
- `layout/`
  - Shared application layouts
- `pages/`
  - Feature-specific application pages

---

# Tech Stack

## Backend

- Kotlin
- Spring Boot
- Hibernate / JPA
- PostgreSQL
- Gradle

## Frontend

- React
- TypeScript
- Axios
- Vite

---

# Project Structure

```sh
hospitalManagement/
├── backend/
│   ├── api/
│   ├── dbRepositories/
│   ├── models/
│   ├── service/
│   └── API_DOCS.md
│
├── frontend/
│   ├── components/
│   ├── layout/
│   ├── pages/
│   └── src/
│
└── README.md
```

---

# Security & Data Protection

This project places a strong focus on the secure handling of sensitive medical and personal information.

Implemented concepts include:

- encryption of personal data
- secure persistence handling
- separation of sensitive business logic
- structured backend layering
- controlled database access through repositories and services

The system is designed with data privacy and maintainability in mind, especially regarding healthcare-related information.

---

# Performance Optimization

To improve scalability and database efficiency, the project uses several optimization strategies:

- JPA Specifications for dynamic and optimized queries
- modular repository design
- structured entity relationships
- optimized database access patterns
- separation of concerns across backend layers

The architecture allows scalable extension and improved maintainability for larger datasets and more complex hospital workflows.

---

# Getting Started

## Prerequisites

Before getting started, ensure your environment contains:

- Java 17+
- Kotlin
- PostgreSQL
- Node.js
- npm
- Gradle

---

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/jshProgrammer/hospitalManagement
```

### 2. Navigate into the project

```bash
cd hospitalManagement
```

### 3. Backend setup

```bash
cd backend
gradle build
```

### 4. Frontend setup

```bash
cd frontend
npm install
```

---

# Running the Application

## Backend

```bash
gradle run
```

## Frontend

```bash
npm run dev
```

---

# API Documentation

Detailed API documentation is available in:

```text
backend/API_DOCS.md
```

The documentation contains information about:

- available endpoints
- request models
- response structures
- supported operations

---
# Team

Contributors:

- [Tom Knoblach](https://github.com/Gottschalk125)

- [Jasmin Wander](https://github.com/xjasx4)

- [David Heppenheimer](https://github.com/davidhepp)

- [Tim Knüttel](https://github.com/timknt)

- [Joshua Pfennig](https://github.com/jshProgrammer)
