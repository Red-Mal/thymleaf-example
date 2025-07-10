CREATE TABLE persons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    passport_id VARCHAR(20) NOT NULL UNIQUE,
    passport_image VARCHAR(255),
    demande_status VARCHAR(20) NOT NULL
);

INSERT INTO persons (first_name, last_name, passport_id, passport_image, demande_status) VALUES
('John', 'Doe', 'P123456789', '/images/sample-passport1.jpg', 'APPROVED'),
('Jane', 'Smith', 'P987654321', '/images/sample-passport2.jpg', 'PENDING'),
('Michael', 'Johnson', 'P456789123', '/images/sample-passport3.jpg', 'IN_REVIEW'),
('Sarah', 'Williams', 'P789123456', '/images/sample-passport4.jpg', 'REJECTED'),
('David', 'Brown', 'P321654987', '/images/sample-passport5.jpg', 'PENDING'); 