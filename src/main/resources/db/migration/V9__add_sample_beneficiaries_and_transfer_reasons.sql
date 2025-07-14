-- V9__add_sample_beneficiaries_and_transfer_reasons.sql

-- Add a sample beneficiary
INSERT INTO beneficiaries (beneficiary_name, swift_code, first_name, city, country, person_id)
VALUES ('John Beneficiary', 'ABCDEF12', 'John', 'Paris', 'France', 1);

-- Add a sample transfer reason
INSERT INTO transfer_reasons (main_beneficiary_name, main_beneficiary_first_name, iban, bic_code, city, person_id)
VALUES ('Doe', 'Jane', 'FR7612345987650123456789014', 'BNPAFRPP', 'Lyon', 1); 