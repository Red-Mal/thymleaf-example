package com.example.thymleaf_example.repository;

import com.example.thymleaf_example.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Basic CRUD operations are automatically provided by JpaRepository
    
    // Search by first name or last name (case insensitive)
    List<Person> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Search by passport ID (case insensitive)
    List<Person> findByPassportIdContainingIgnoreCase(String passportId);
} 