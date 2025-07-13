package com.example.thymleaf_example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Child {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Child name is required")
    @Size(min = 2, max = 100, message = "Child name must be between 2 and 100 characters")
    @Column(name = "child_name", nullable = false)
    private String childName;
    
    @NotBlank(message = "Child passport ID is required")
    @Size(min = 5, max = 20, message = "Child passport ID must be between 5 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Child passport ID can only contain uppercase letters and numbers")
    @Column(name = "child_passport_id", nullable = false)
    private String childPassportId;
    
    @Column(name = "child_address", length = 500)
    private String childAddress;
    
    @Column(name = "child_birth_date")
    private LocalDate childBirthDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
} 