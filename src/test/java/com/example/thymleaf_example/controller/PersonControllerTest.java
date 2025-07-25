package com.example.thymleaf_example.controller;

import com.example.thymleaf_example.model.Person;
import com.example.thymleaf_example.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private Person testPerson;
    private Page<Person> personPage;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setId(1L);
        testPerson.setFirstName("John");
        testPerson.setLastName("Doe");
        testPerson.setPassportId("P123456789");
        testPerson.setDemandeStatus(Person.DemandeStatus.PENDING);

        List<Person> persons = Arrays.asList(testPerson);
        Pageable pageable = PageRequest.of(0, 10);
        personPage = new PageImpl<>(persons, pageable, 1);
    }

    @Test
    void listPersons_ShouldReturnPersonsPage() throws Exception {
        // Arrange
        when(personService.findAllPaginated(any(Pageable.class))).thenReturn(personPage);

        // Act & Assert
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(view().name("persons"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("statuses"));

        verify(personService).findAllPaginated(any(Pageable.class));
    }

    @Test
    void listPersons_WithPagination_ShouldReturnCorrectPage() throws Exception {
        // Arrange
        when(personService.findAllPaginated(any(Pageable.class))).thenReturn(personPage);

        // Act & Assert
        mockMvc.perform(get("/persons")
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("persons"));

        verify(personService).findAllPaginated(any(Pageable.class));
    }

    @Test
    void searchPersons_WithNameSearch_ShouldReturnMatchingPersons() throws Exception {
        // Arrange
        List<Person> searchResults = Arrays.asList(testPerson);
        Page<Person> searchPage = new PageImpl<>(searchResults, PageRequest.of(0, 10), 1);
        when(personService.searchByName("John")).thenReturn(searchResults);

        // Act & Assert
        mockMvc.perform(get("/persons/search")
                .param("searchType", "name")
                .param("searchTerm", "John"))
                .andExpect(status().isOk())
                .andExpect(view().name("persons"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attribute("searchType", "name"))
                .andExpect(model().attribute("searchTerm", "John"));

        verify(personService).searchByName("John");
    }

    @Test
    void searchPersons_WithPassportIdSearch_ShouldReturnMatchingPersons() throws Exception {
        // Arrange
        List<Person> searchResults = Arrays.asList(testPerson);
        Page<Person> searchPage = new PageImpl<>(searchResults, PageRequest.of(0, 10), 1);
        when(personService.searchByPassportId("P123")).thenReturn(searchResults);

        // Act & Assert
        mockMvc.perform(get("/persons/search")
                .param("searchType", "passportId")
                .param("searchTerm", "P123"))
                .andExpect(status().isOk())
                .andExpect(view().name("persons"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attribute("searchType", "passportId"))
                .andExpect(model().attribute("searchTerm", "P123"));

        verify(personService).searchByPassportId("P123");
    }

    @Test
    void searchPersons_WithEmptySearchTerm_ShouldReturnAllPersons() throws Exception {
        // Arrange
        when(personService.findAllPaginated(any(Pageable.class))).thenReturn(personPage);

        // Act & Assert
        mockMvc.perform(get("/persons/search")
                .param("searchType", "name")
                .param("searchTerm", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("persons"));

        verify(personService).findAllPaginated(any(Pageable.class));
    }

    @Test
    void updateStatus_WithValidData_ShouldRedirectToPersons() throws Exception {
        // Arrange
        when(personService.updateStatus(1L, Person.DemandeStatus.APPROVED)).thenReturn(testPerson);

        // Act & Assert
        mockMvc.perform(post("/persons/update-status")
                .param("personId", "1")
                .param("newStatus", "APPROVED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/persons"));

        verify(personService).updateStatus(1L, Person.DemandeStatus.APPROVED);
    }

    @Test
    void updateStatus_WithInvalidPersonId_ShouldHandleException() throws Exception {
        // Arrange
        when(personService.updateStatus(999L, Person.DemandeStatus.APPROVED))
                .thenThrow(new RuntimeException("Person not found with id: 999"));

        // Act & Assert
        mockMvc.perform(post("/persons/update-status")
                .param("personId", "999")
                .param("newStatus", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(personService).updateStatus(999L, Person.DemandeStatus.APPROVED);
    }
} 