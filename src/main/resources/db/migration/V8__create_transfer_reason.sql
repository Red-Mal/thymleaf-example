CREATE TABLE transfer_reasons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    main_beneficiary_name VARCHAR(100),
    main_beneficiary_first_name VARCHAR(100),
    iban VARCHAR(50),
    bic_code VARCHAR(50),
    city VARCHAR(100),
    person_id BIGINT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
); 