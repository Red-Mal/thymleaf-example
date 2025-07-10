package com.example.thymleaf_example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    
    @Column(name = "passport_image")
    private String passportImage; // This will store the image path or base64 data
    
    @Enumerated(EnumType.STRING)
    @Column(name = "demande_status", nullable = false)
    private DemandeStatus demandeStatus = DemandeStatus.PENDING;
    
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