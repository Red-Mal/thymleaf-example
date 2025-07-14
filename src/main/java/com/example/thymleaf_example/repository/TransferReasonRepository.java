package com.example.thymleaf_example.repository;

import com.example.thymleaf_example.model.TransferReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferReasonRepository extends JpaRepository<TransferReason, Long> {
} 