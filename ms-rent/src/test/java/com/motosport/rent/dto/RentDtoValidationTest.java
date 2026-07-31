package com.motosport.rent.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RentDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRentDto_shouldHaveNoViolations() {
        RentDto dto = new RentDto(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(5), "Sin observaciones");

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidRentDto_shouldReportViolations() {
        String observacionMuyLarga = "a".repeat(300);
        RentDto dto = new RentDto(null, null, null,
                LocalDate.now().minusDays(1), null, observacionMuyLarga);

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream()
            .map(v -> v.getPropertyPath().toString())
            .collect(Collectors.toSet());

        assertFalse(violations.isEmpty());
        assertTrue(fields.contains("bikeId"));
        assertTrue(fields.contains("customerId"));
        assertTrue(fields.contains("fechaInicio"));
        assertTrue(fields.contains("fechaFin"));
        assertTrue(fields.contains("observacion"));
    }
}