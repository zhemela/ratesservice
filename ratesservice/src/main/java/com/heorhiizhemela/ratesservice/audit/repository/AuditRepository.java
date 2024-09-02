package com.heorhiizhemela.ratesservice.audit.repository;

import com.heorhiizhemela.ratesservice.audit.entity.CurrencyExchangeRateAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<CurrencyExchangeRateAudit, Long> {
}
