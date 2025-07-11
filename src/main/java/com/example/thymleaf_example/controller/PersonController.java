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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.util.Random;


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

    @GetMapping("/{id}/passport-image")
    public ResponseEntity<byte[]> downloadPassportImage(@PathVariable Long id) throws IOException {
        Person person = personService.findById(id);
        byte[] imageData = person.getPassportImageBlob();
        if (imageData == null || imageData.length == 0) {
            // Serve a random image from static/images
            String[] randomImages = {"static/images/random1.jpg", "static/images/random2.jpg", "static/images/random3.jpg"};
            int idx = new Random().nextInt(randomImages.length);
            ClassPathResource imgFile = new ClassPathResource(randomImages[idx]);
            byte[] randomImageData = StreamUtils.copyToByteArray(imgFile.getInputStream());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=random.jpg")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(randomImageData);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=passport_" + id + ".jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }

    @PostMapping("/update-person")
    public String updatePerson(@RequestParam Long personId,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String passportId,
                               @RequestParam Person.DemandeStatus demandeStatus) {
        try {
            Person person = personService.findById(personId);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setPassportId(passportId);
            person.setDemandeStatus(demandeStatus);
            personService.save(person);
        } catch (Exception e) {
            throw e;
        }
        return "redirect:/persons";
    }

    // Remove /add-sample-data endpoint and related logic

    // Temporary endpoint to migrate images from file path to BLOB
    @GetMapping("/migrate-images-to-blob")
    public String migrateImagesToBlob() {
        personService.migrateImagesToBlob();
        return "redirect:/persons";
    }
} 