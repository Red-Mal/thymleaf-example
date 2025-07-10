package com.example.thymleaf_example.controller;

import com.example.thymleaf_example.model.Person;
import com.example.thymleaf_example.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public String listPersons(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Person> personPage = personService.findAllPaginated(pageable);
        
        model.addAttribute("persons", personPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", personPage.getTotalPages());
        model.addAttribute("totalItems", personPage.getTotalElements());
        model.addAttribute("statuses", Person.DemandeStatus.values());
        return "persons";
    }

    @GetMapping("/search")
    public String searchPersons(@RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String searchTerm,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Person> personPage;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            switch (searchType) {
                case "name":
                    List<Person> nameResults = personService.searchByName(searchTerm.trim());
                    personPage = new PageImpl<>(nameResults, pageable, nameResults.size());
                    break;
                case "passportId":
                    List<Person> passportResults = personService.searchByPassportId(searchTerm.trim());
                    personPage = new PageImpl<>(passportResults, pageable, passportResults.size());
                    break;
                default:
                    personPage = personService.findAllPaginated(pageable);
            }
        } else {
            personPage = personService.findAllPaginated(pageable);
        }
        
        model.addAttribute("persons", personPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", personPage.getTotalPages());
        model.addAttribute("totalItems", personPage.getTotalElements());
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("statuses", Person.DemandeStatus.values());
        return "persons";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long personId, 
                              @RequestParam Person.DemandeStatus newStatus) {
        try {
            personService.updateStatus(personId, newStatus);
        } catch (Exception e) {
            // Error will be handled by GlobalExceptionHandler
            throw e;
        }
        return "redirect:/persons";
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam Long personId, 
                             @RequestParam("passportImage") MultipartFile file) {
        try {
            personService.uploadPassportImage(personId, file);
        } catch (Exception e) {
            // Error will be handled by GlobalExceptionHandler
            throw e;
        }
        return "redirect:/persons";
    }

    @PostMapping("/update-person")
    public String updatePerson(@RequestParam Long personId,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String passportId,
                               @RequestParam Person.DemandeStatus demandeStatus,
                               @RequestParam(value = "passportImage", required = false) MultipartFile passportImage) {
        try {
            Person personDetails = new Person();
            personDetails.setFirstName(firstName);
            personDetails.setLastName(lastName);
            personDetails.setPassportId(passportId);
            personDetails.setDemandeStatus(demandeStatus);
            
            personService.updatePersonWithImage(personId, personDetails, passportImage);
        } catch (Exception e) {
            // Error will be handled by GlobalExceptionHandler
            throw e;
        }
        return "redirect:/persons";
    }

    @GetMapping("/add-sample-data")
    public String addSampleData() {
        // Add some sample data if the table is empty
        if (personService.findAll().isEmpty()) {
            Person person1 = new Person();
            person1.setFirstName("John");
            person1.setLastName("Doe");
            person1.setPassportId("P123456789");
            person1.setPassportImage("/images/sample-passport1.jpg");
            person1.setDemandeStatus(Person.DemandeStatus.APPROVED);
            personService.save(person1);

            Person person2 = new Person();
            person2.setFirstName("Jane");
            person2.setLastName("Smith");
            person2.setPassportId("P987654321");
            person2.setPassportImage("/images/sample-passport2.jpg");
            person2.setDemandeStatus(Person.DemandeStatus.PENDING);
            personService.save(person2);

            Person person3 = new Person();
            person3.setFirstName("Michael");
            person3.setLastName("Johnson");
            person3.setPassportId("P456789123");
            person3.setPassportImage("/images/sample-passport3.jpg");
            person3.setDemandeStatus(Person.DemandeStatus.IN_REVIEW);
            personService.save(person3);

            Person person4 = new Person();
            person4.setFirstName("Sarah");
            person4.setLastName("Williams");
            person4.setPassportId("P789123456");
            person4.setPassportImage("/images/sample-passport4.jpg");
            person4.setDemandeStatus(Person.DemandeStatus.REJECTED);
            personService.save(person4);

            Person person5 = new Person();
            person5.setFirstName("David");
            person5.setLastName("Brown");
            person5.setPassportId("P321654987");
            person5.setPassportImage("/images/sample-passport5.jpg");
            person5.setDemandeStatus(Person.DemandeStatus.PENDING);
            personService.save(person5);
        }
        return "redirect:/persons";
    }
} 