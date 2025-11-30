package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {

    List<CreditRequest> findByUserId(Long userId);
}
