-- Add new fields to persons table
ALTER TABLE persons ADD COLUMN email VARCHAR(255);
ALTER TABLE persons ADD COLUMN birth_date DATE;
ALTER TABLE persons ADD COLUMN address VARCHAR(500);

-- Create children table
CREATE TABLE children (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_name VARCHAR(100) NOT NULL,
    child_passport_id VARCHAR(20) NOT NULL,
    child_address VARCHAR(500),
    child_birth_date DATE,
    person_id BIGINT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Create beneficiaries table
CREATE TABLE beneficiaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    beneficiary_name VARCHAR(100) NOT NULL,
    swift_code VARCHAR(11) NOT NULL,
    person_id BIGINT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
); 