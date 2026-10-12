package com.atharva.educationcrm.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.atharva.educationcrm.entity.Payment;
import com.atharva.educationcrm.entity.Payment.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @EntityGraph(attributePaths = {"student", "course", "enrollment"})
    List<Payment> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"student", "course", "enrollment"})
    List<Payment> findByStudentEmailIgnoreCaseOrderByCreatedAtDesc(String email);

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.status = :status")
    BigDecimal sumAmountByStatus(PaymentStatus status);
}
