-- Migration: Add encryption and blind indexing support

-- ===== PERSON TABLE =====

-- Rename existing columns to _encrypted
ALTER TABLE person RENAME COLUMN firstName TO firstName_encrypted;
ALTER TABLE person RENAME COLUMN lastName TO lastName_encrypted;
ALTER TABLE person RENAME COLUMN phone TO phone_encrypted;
ALTER TABLE person RENAME COLUMN email TO email_encrypted;
ALTER TABLE person RENAME COLUMN street TO street_encrypted;
ALTER TABLE person RENAME COLUMN city TO city_encrypted;
ALTER TABLE person RENAME COLUMN birthday TO birthday_encrypted;
ALTER TABLE person RENAME COLUMN plz TO plz_encrypted;

-- Add hash columns for blind indexing
ALTER TABLE person ADD COLUMN firstName_hash VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE person ADD COLUMN lastName_hash VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE person ADD COLUMN email_hash VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE person ADD COLUMN city_hash VARCHAR(255) DEFAULT '';
ALTER TABLE person ADD COLUMN phone_hash VARCHAR(255);
ALTER TABLE person ADD COLUMN street_hash VARCHAR(255);
ALTER TABLE person ADD COLUMN birthday_hash VARCHAR(255);

-- Add unique constraint on email_hash
ALTER TABLE person ADD CONSTRAINT uq_person_email_hash UNIQUE (email_hash);

-- Drop old indexes
DROP INDEX IF EXISTS idx_person_city_id;
DROP INDEX IF EXISTS idx_person_last_first_id;

-- Create new indexes on hash columns
CREATE INDEX idx_person_firstName_hash ON person(firstName_hash);
CREATE INDEX idx_person_lastName_hash ON person(lastName_hash);
CREATE INDEX idx_person_email_hash ON person(email_hash);
CREATE INDEX idx_person_city_hash ON person(city_hash);
CREATE INDEX idx_person_lastName_firstName_hash ON person(lastName_hash, firstName_hash);

-- ===== DIAGNOSIS TABLE =====

-- Rename disease column
ALTER TABLE diagnosis RENAME COLUMN disease TO disease_encrypted;

-- Add hash column for blind indexing
ALTER TABLE diagnosis ADD COLUMN disease_hash VARCHAR(255) NOT NULL DEFAULT '';

-- Drop old index
DROP INDEX IF EXISTS idx_diagnosis_disease_date;

-- Create new index on hash column
CREATE INDEX idx_diagnosis_disease_hash ON diagnosis(disease_hash);
