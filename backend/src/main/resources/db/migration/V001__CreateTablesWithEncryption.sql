-- V001__CreateTablesWithEncryption.sql
-- Create database tables with encryption support from the start

-- Create ENUM types
CREATE TYPE IF NOT EXISTS gender AS ENUM ('m', 'f', 'd');
CREATE TYPE IF NOT EXISTS doctors_type AS ENUM ('RESIDENT', 'STAFF', 'CHIEF', 'INTERN');
CREATE TYPE IF NOT EXISTS dose_frequency AS ENUM ('ONCE_DAILY', 'TWICE_DAILY', 'THREE_TIMES_DAILY', 'FOUR_TIMES_DAILY', 'EVERY_SIX_HOURS', 'AS_NEEDED');

-- Create PERSON table with encrypted and hash columns
CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    gender gender NOT NULL,
    firstName_encrypted VARCHAR(255),
    firstName_hash VARCHAR(255) NOT NULL DEFAULT '',
    lastName_encrypted VARCHAR(255),
    lastName_hash VARCHAR(255) NOT NULL DEFAULT '',
    email_encrypted VARCHAR(255),
    email_hash VARCHAR(255) NOT NULL UNIQUE,
    phone_encrypted VARCHAR(255),
    phone_hash VARCHAR(255),
    street_encrypted VARCHAR(255),
    street_hash VARCHAR(255),
    street_no INT NOT NULL DEFAULT 0,
    country VARCHAR(255),
    city_encrypted VARCHAR(255),
    city_hash VARCHAR(255) DEFAULT '',
    plz_encrypted INT,
    birthday_encrypted DATE,
    birthday_hash VARCHAR(255)
);

-- Create indexes on hash columns for fast search
CREATE INDEX IF NOT EXISTS idx_person_firstName_hash ON person(firstName_hash);
CREATE INDEX IF NOT EXISTS idx_person_lastName_hash ON person(lastName_hash);
CREATE INDEX IF NOT EXISTS idx_person_email_hash ON person(email_hash);
CREATE INDEX IF NOT EXISTS idx_person_city_hash ON person(city_hash);
CREATE INDEX IF NOT EXISTS idx_person_lastName_firstName_hash ON person(lastName_hash, firstName_hash);

-- Create EMPLOYEE table
CREATE TABLE IF NOT EXISTS employee (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT UNIQUE NOT NULL REFERENCES person(id),
    department_id BIGINT
);

CREATE INDEX IF NOT EXISTS idx_employee_department_id ON employee(department_id);

-- Create DOCTOR table (extends EMPLOYEE)
CREATE TABLE IF NOT EXISTS doctor (
    id BIGINT PRIMARY KEY REFERENCES employee(id),
    work_phone VARCHAR(255),
    type doctors_type
);

-- Create PATIENT table
CREATE TABLE IF NOT EXISTS patient (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT UNIQUE NOT NULL REFERENCES person(id)
);

-- Create DOSE table
CREATE TABLE IF NOT EXISTS dose (
    id BIGSERIAL PRIMARY KEY,
    frequency dose_frequency
);

-- Create DRUG table
CREATE TABLE IF NOT EXISTS drug (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- Create MEDICATION table
CREATE TABLE IF NOT EXISTS medication (
    id BIGSERIAL PRIMARY KEY,
    dose_id BIGINT REFERENCES dose(id),
    drug_id BIGINT REFERENCES drug(id),
    started DATE,
    ended DATE
);

-- Create DIAGNOSIS table with encrypted disease field and hash
CREATE TABLE IF NOT EXISTS diagnosis (
    id BIGSERIAL PRIMARY KEY,
    disease_encrypted VARCHAR(255),
    disease_hash VARCHAR(255) NOT NULL DEFAULT '',
    medication BIGINT REFERENCES medication(id),
    diagnosed_by BIGINT NOT NULL REFERENCES doctor(id),
    diagnosed_patient BIGINT NOT NULL REFERENCES patient(id),
    diagnosed_at DATE
);

-- Create indexes on diagnosis
CREATE INDEX IF NOT EXISTS idx_diagnosis_disease_hash ON diagnosis(disease_hash);
CREATE INDEX IF NOT EXISTS idx_diagnosis_date_id ON diagnosis(diagnosed_at, id);
CREATE INDEX IF NOT EXISTS idx_diagnosis_doctor_date_id ON diagnosis(diagnosed_by, diagnosed_at, id);
CREATE INDEX IF NOT EXISTS idx_diagnosis_medication ON diagnosis(medication);
CREATE INDEX IF NOT EXISTS idx_diagnosis_patient_date_id ON diagnosis(diagnosed_patient, diagnosed_at, id);
