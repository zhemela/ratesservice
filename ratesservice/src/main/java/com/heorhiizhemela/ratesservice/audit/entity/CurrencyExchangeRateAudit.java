package com.heorhiizhemela.ratesservice.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency_exchange_rate_audit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "change_date")
    private LocalDateTime changeDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
}
