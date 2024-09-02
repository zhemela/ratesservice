package com.heorhiizhemela.ratesservice.audit.service;

import com.heorhiizhemela.ratesservice.audit.entity.ActionType;
import com.heorhiizhemela.ratesservice.audit.entity.CurrencyExchangeRateAudit;
import com.heorhiizhemela.ratesservice.audit.repository.AuditRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
class AuditServiceTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("audit")
            .withUsername("user")
            .withPassword("pass")
            .withUrlParam("stringtype", "unspecified");

    static {
        Startables.deepStart(postgreSQLContainer);
    }

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private AuditService auditService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @AfterEach
    void tearDown() {
        auditRepository.deleteAll();
    }

    @Test
    void testAuditRateChange() {
        String currencyName = "USD";
        BigDecimal rate = BigDecimal.valueOf(1.0);
        ActionType actionType = ActionType.CREATE;

        auditService.auditRateChange(currencyName, rate, actionType);

        List<CurrencyExchangeRateAudit> audits = auditRepository.findAll();
        assertEquals(1, audits.size());

        CurrencyExchangeRateAudit audit = audits.get(0);
        assertEquals(currencyName, audit.getCurrencyName());
        assertEquals(actionType, audit.getActionType());
        assertNotNull(audit.getChangeDate());
        assertTrue(rate.compareTo(audit.getRate()) == 0);
        assertEquals(LocalDateTime.now().getDayOfYear(), audit.getChangeDate().getDayOfYear());
    }
}
