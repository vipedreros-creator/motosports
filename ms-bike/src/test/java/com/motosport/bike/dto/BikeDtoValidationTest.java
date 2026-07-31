package com.motosport.bike.dto;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BikeDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validBikeDto_shouldHaveNoViolations() {
        BikeDto dto = new BikeDto(1L, "Kawasaki", "Ninja 400", "ABC123", 4500000, 2022, "Verde", 12000, true);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidBikeDto_shouldReportViolations() {
        BikeDto dto = new BikeDto(null, "", "", "AB", -100, 1800, "", -5, null);

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream()
            .map(v -> v.getPropertyPath().toString())
            .collect(Collectors.toSet());

        assertFalse(violations.isEmpty());
        assertTrue(fields.contains("marca"));
        assertTrue(fields.contains("modelo"));
        assertTrue(fields.contains("patente"));
        assertTrue(fields.contains("valor"));
        assertTrue(fields.contains("annio"));
        assertTrue(fields.contains("color"));
        assertTrue(fields.contains("kilometraje"));
        assertTrue(fields.contains("disponibilidad"));
    }
}