ALTER TABLE persons ADD COLUMN code_client VARCHAR(100) UNIQUE;
ALTER TABLE persons ADD COLUMN type_operation VARCHAR(100);
ALTER TABLE persons ADD COLUMN creation_date DATE;
ALTER TABLE persons ADD COLUMN dossier_status VARCHAR(100);
ALTER TABLE persons ADD COLUMN dossier_name VARCHAR(100);
ALTER TABLE persons ADD COLUMN phone VARCHAR(50); 