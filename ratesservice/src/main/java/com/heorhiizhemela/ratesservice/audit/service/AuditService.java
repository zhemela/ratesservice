package com.heorhiizhemela.ratesservice.audit.service;

import com.heorhiizhemela.ratesservice.audit.entity.ActionType;
import com.heorhiizhemela.ratesservice.audit.entity.CurrencyExchangeRateAudit;
import com.heorhiizhemela.ratesservice.audit.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;

    public void auditRateChange(String currencyName, BigDecimal rate, ActionType actionType) {
        CurrencyExchangeRateAudit audit = CurrencyExchangeRateAudit.builder()
                .currencyName(currencyName)
                .rate(rate)
                .changeDate(LocalDateTime.now())
                .actionType(actionType)
                .build();
        auditRepository.save(audit);
    }
}
