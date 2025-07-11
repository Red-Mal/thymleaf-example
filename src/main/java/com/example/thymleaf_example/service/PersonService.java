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
import java.nio.file.StandardOpenOption;
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

    // Add migration method
    public void migrateImagesToBlob() {
        List<Person> persons = personRepository.findAll();
        for (Person person : persons) {
            if (person.getPassportImageBlob() == null && person.getPassportImage() != null && !person.getPassportImage().isEmpty()) {
                String imagePath = person.getPassportImage();
                // Only migrate if path is not a sample image
                if (imagePath.startsWith("/uploads/") || imagePath.startsWith("/images/")) {
                    try {
                        Path path = Paths.get("src/main/resources/static" + imagePath);
                        if (Files.exists(path)) {
                            byte[] imageBytes = Files.readAllBytes(path);
                            person.setPassportImageBlob(imageBytes);
                            personRepository.save(person);
                        }
                    } catch (Exception e) {
                        // Log and continue
                        System.err.println("Failed to migrate image for person ID " + person.getId() + ": " + e.getMessage());
                    }
                }
            }
        }
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