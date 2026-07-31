package com.motosport.customer.dto;

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

public class CustomerDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCustomerDto_shouldHaveNoViolations() {
        CustomerDto dto = new CustomerDto(1L, "12345678-9", "Juan", "Perez",
                987654321, "juan@correo.com", "LIC12345",
                LocalDate.now().plusYears(2), LocalDate.now());

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidCustomerDto_shouldReportViolations() {
        CustomerDto dto = new CustomerDto(null, "", "", "",
                null, "correo-invalido", "",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream()
            .map(v -> v.getPropertyPath().toString())
            .collect(Collectors.toSet());

        assertFalse(violations.isEmpty());
        assertTrue(fields.contains("rut"));
        assertTrue(fields.contains("nombre"));
        assertTrue(fields.contains("apellidos"));
        assertTrue(fields.contains("numeroTelefono"));
        assertTrue(fields.contains("correo"));
        assertTrue(fields.contains("nroLicencia"));
        assertTrue(fields.contains("fechaVencimiento"));
        assertTrue(fields.contains("fechaRegistro"));
    }
}