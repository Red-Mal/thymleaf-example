package com.example.thymleaf_example.service;

import com.example.thymleaf_example.model.Person;
import com.example.thymleaf_example.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person testPerson;
    private Person validPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setId(1L);
        testPerson.setFirstName("John");
        testPerson.setLastName("Doe");
        testPerson.setPassportId("P123456789");
        testPerson.setDemandeStatus(Person.DemandeStatus.PENDING);

        validPerson = new Person();
        validPerson.setFirstName("Jane");
        validPerson.setLastName("Smith");
        validPerson.setPassportId("P987654321");
        validPerson.setDemandeStatus(Person.DemandeStatus.APPROVED);
    }

    @Test
    void findAllPaginated_ShouldReturnPageOfPersons() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Person> persons = Arrays.asList(testPerson);
        Page<Person> expectedPage = new PageImpl<>(persons, pageable, 1);
        when(personRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Person> result = personService.findAllPaginated(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testPerson, result.getContent().get(0));
        verify(personRepository).findAll(pageable);
    }

    @Test
    void findAll_ShouldReturnAllPersons() {
        // Arrange
        List<Person> expectedPersons = Arrays.asList(testPerson);
        when(personRepository.findAll()).thenReturn(expectedPersons);

        // Act
        List<Person> result = personService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPerson, result.get(0));
        verify(personRepository).findAll();
    }

    @Test
    void findById_WhenPersonExists_ShouldReturnPerson() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(testPerson));

        // Act
        Person result = personService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testPerson, result);
        verify(personRepository).findById(1L);
    }

    @Test
    void findById_WhenPersonDoesNotExist_ShouldThrowException() {
        // Arrange
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> personService.findById(999L));
        assertEquals("Person not found with id: 999", exception.getMessage());
        verify(personRepository).findById(999L);
    }

    @Test
    void save_WithValidPerson_ShouldSaveAndReturnPerson() {
        // Arrange
        when(personRepository.save(any(Person.class))).thenReturn(validPerson);

        // Act
        Person result = personService.save(validPerson);

        // Assert
        assertNotNull(result);
        assertEquals(validPerson, result);
        verify(personRepository).save(validPerson);
    }

    @Test
    void save_WithNullPerson_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.save(null));
        assertEquals("Person cannot be null", exception.getMessage());
        verify(personRepository, never()).save(any());
    }

    @Test
    void save_WithEmptyFirstName_ShouldThrowException() {
        // Arrange
        validPerson.setFirstName("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.save(validPerson));
        assertEquals("First name is required", exception.getMessage());
        verify(personRepository, never()).save(any());
    }

    @Test
    void save_WithEmptyLastName_ShouldThrowException() {
        // Arrange
        validPerson.setLastName("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.save(validPerson));
        assertEquals("Last name is required", exception.getMessage());
        verify(personRepository, never()).save(any());
    }

    @Test
    void save_WithEmptyPassportId_ShouldThrowException() {
        // Arrange
        validPerson.setPassportId("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.save(validPerson));
        assertEquals("Passport ID is required", exception.getMessage());
        verify(personRepository, never()).save(any());
    }

    @Test
    void save_WithNullStatus_ShouldThrowException() {
        // Arrange
        validPerson.setDemandeStatus(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.save(validPerson));
        assertEquals("Status is required", exception.getMessage());
        verify(personRepository, never()).save(any());
    }

    @Test
    void update_WithValidData_ShouldUpdateAndReturnPerson() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(testPerson));
        when(personRepository.save(any(Person.class))).thenReturn(testPerson);

        // Act
        Person result = personService.update(1L, validPerson);

        // Assert
        assertNotNull(result);
        verify(personRepository).findById(1L);
        verify(personRepository).save(testPerson);
    }

    @Test
    void update_WhenPersonNotFound_ShouldThrowException() {
        // Arrange
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> personService.update(999L, validPerson));
        assertEquals("Person not found with id: 999", exception.getMessage());
        verify(personRepository).findById(999L);
        verify(personRepository, never()).save(any());
    }

    @Test
    void deleteById_WhenPersonExists_ShouldDeletePerson() {
        // Arrange
        when(personRepository.existsById(1L)).thenReturn(true);
        doNothing().when(personRepository).deleteById(1L);

        // Act
        personService.deleteById(1L);

        // Assert
        verify(personRepository).existsById(1L);
        verify(personRepository).deleteById(1L);
    }

    @Test
    void deleteById_WhenPersonDoesNotExist_ShouldThrowException() {
        // Arrange
        when(personRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> personService.deleteById(999L));
        assertEquals("Person not found with id: 999", exception.getMessage());
        verify(personRepository).existsById(999L);
        verify(personRepository, never()).deleteById(any());
    }

    @Test
    void searchByName_WithValidTerm_ShouldReturnMatchingPersons() {
        // Arrange
        List<Person> expectedPersons = Arrays.asList(testPerson);
        when(personRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "John"))
            .thenReturn(expectedPersons);

        // Act
        List<Person> result = personService.searchByName("John");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPerson, result.get(0));
        verify(personRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "John");
    }

    @Test
    void searchByName_WithEmptyTerm_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.searchByName(""));
        assertEquals("Search term cannot be empty", exception.getMessage());
        verify(personRepository, never()).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(any(), any());
    }

    @Test
    void searchByName_WithNullTerm_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.searchByName(null));
        assertEquals("Search term cannot be empty", exception.getMessage());
        verify(personRepository, never()).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(any(), any());
    }

    @Test
    void searchByPassportId_WithValidTerm_ShouldReturnMatchingPersons() {
        // Arrange
        List<Person> expectedPersons = Arrays.asList(testPerson);
        when(personRepository.findByPassportIdContainingIgnoreCase("P123")).thenReturn(expectedPersons);

        // Act
        List<Person> result = personService.searchByPassportId("P123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPerson, result.get(0));
        verify(personRepository).findByPassportIdContainingIgnoreCase("P123");
    }

    @Test
    void searchByPassportId_WithEmptyTerm_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.searchByPassportId(""));
        assertEquals("Search term cannot be empty", exception.getMessage());
        verify(personRepository, never()).findByPassportIdContainingIgnoreCase(any());
    }

    @Test
    void updateStatus_WithValidData_ShouldUpdateStatus() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(testPerson));
        when(personRepository.save(any(Person.class))).thenReturn(testPerson);

        // Act
        Person result = personService.updateStatus(1L, Person.DemandeStatus.APPROVED);

        // Assert
        assertNotNull(result);
        assertEquals(Person.DemandeStatus.APPROVED, testPerson.getDemandeStatus());
        verify(personRepository).findById(1L);
        verify(personRepository).save(testPerson);
    }

    @Test
    void updateStatus_WhenPersonNotFound_ShouldThrowException() {
        // Arrange
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> personService.updateStatus(999L, Person.DemandeStatus.APPROVED));
        assertEquals("Person not found with id: 999", exception.getMessage());
        verify(personRepository).findById(999L);
        verify(personRepository, never()).save(any());
    }
} 