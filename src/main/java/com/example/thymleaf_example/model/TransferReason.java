package com.example.thymleaf_example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "transfer_reasons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_beneficiary_name")
    private String mainBeneficiaryName;

    @Column(name = "main_beneficiary_first_name")
    private String mainBeneficiaryFirstName;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic_code")
    private String bicCode;

    @Column(name = "city")
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
} 