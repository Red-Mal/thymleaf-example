package com.example.thymleaf_example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name can only contain letters and spaces")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name can only contain letters and spaces")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @NotBlank(message = "Passport ID is required")
    @Size(min = 5, max = 20, message = "Passport ID must be between 5 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Passport ID can only contain uppercase letters and numbers")
    @Column(name = "passport_id", unique = true, nullable = false)
    private String passportId;
    
    @Email(message = "Please provide a valid email address")
    @Column(name = "email")
    private String email;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "address", length = 500)
    private String address;
    
    @Column(name = "passport_image")
    private String passportImage; // This will be deprecated

    @Lob
    @Column(name = "passport_image_blob")
    private byte[] passportImageBlob;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "demande_status", nullable = false)
    private DemandeStatus demandeStatus = DemandeStatus.PENDING;
    
    @Column(name = "code_client", unique = true)
    private String codeClient;

    @Column(name = "type_operation")
    private String typeOperation;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "dossier_status")
    private String dossierStatus;

    @Column(name = "dossier_name")
    private String dossierName;

    @Column(name = "phone")
    private String phone;
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Child> children = new ArrayList<>();
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Beneficiary> beneficiaries = new ArrayList<>();
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransferReason> transferReasons = new ArrayList<>();
    
    public enum DemandeStatus {
        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        IN_REVIEW("In Review");
        
        private final String displayName;
        
        DemandeStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
} 