package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.FinancialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialDataRepository extends JpaRepository<FinancialData, Long> {

    List<FinancialData> findByUserId(Long userId);
}
