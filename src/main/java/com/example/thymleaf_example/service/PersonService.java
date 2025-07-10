package com.example.thymleaf_example.service;

import com.example.thymleaf_example.model.Person;
import com.example.thymleaf_example.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Page<Person> findAllPaginated(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    public Person save(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getFirstName() == null || person.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (person.getLastName() == null || person.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (person.getPassportId() == null || person.getPassportId().trim().isEmpty()) {
            throw new IllegalArgumentException("Passport ID is required");
        }
        if (person.getDemandeStatus() == null) {
            throw new IllegalArgumentException("Status is required");
        }
        return personRepository.save(person);
    }

    public List<Person> searchByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return personRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm.trim(), searchTerm.trim());
    }

    public List<Person> searchByPassportId(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return personRepository.findByPassportIdContainingIgnoreCase(searchTerm.trim());
    }

    public Person updateStatus(Long id, Person.DemandeStatus newStatus) {
        Person person = findById(id);
        person.setDemandeStatus(newStatus);
        return personRepository.save(person);
    }

    public Person uploadPassportImage(Long id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        Person person = findById(id);
        
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = "passport_" + id + "_" + UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // Update person record with new image path
            person.setPassportImage("/uploads/" + newFilename);
            return personRepository.save(person);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    public Person updatePersonWithImage(Long id, Person personDetails, MultipartFile passportImage) {
        Person person = findById(id);
        
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setPassportId(personDetails.getPassportId());
        person.setDemandeStatus(personDetails.getDemandeStatus());
        
        if (passportImage != null && !passportImage.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String originalFilename = passportImage.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = "passport_" + id + "_" + UUID.randomUUID().toString() + fileExtension;
                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(passportImage.getInputStream(), filePath);
                person.setPassportImage("/uploads/" + newFilename);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
            }
        }
        
        return personRepository.save(person);
    }

    public Person update(Long id, Person personDetails) {
        Person person = findById(id);
        
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setPassportId(personDetails.getPassportId());
        person.setDemandeStatus(personDetails.getDemandeStatus());
        
        return personRepository.save(person);
    }

    public void deleteById(Long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }
} 