package com.example.thymleaf_example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "beneficiaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Beneficiary name is required")
    @Size(min = 2, max = 100, message = "Beneficiary name must be between 2 and 100 characters")
    @Column(name = "beneficiary_name", nullable = false)
    private String beneficiaryName;
    
    @NotBlank(message = "Swift code is required")
    @Size(min = 8, max = 11, message = "Swift code must be between 8 and 11 characters")
    @Column(name = "swift_code", nullable = false)
    private String swiftCode;
    
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
} 