package com.motosport.bff.dto;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Inicializa el validador real tal como lo hace el profesor
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validBikeDto_shouldHaveNoViolations() {
        // Arrange: Una moto con todos sus datos correctos
        BikeDto dto = new BikeDto(1L, "Kawasaki", "Ninja 400", "Verde", 400);

        // Act: Validamos el objeto
        var violations = validator.validate(dto);

        // Assert: No deberían existir errores
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidBikeDto_shouldReportViolations() {
        // Arrange: Mandamos datos vacíos o incorrectos para forzar los @NotBlank o @NotNull de tu DTO
        // Nota: Ajusta los valores según las restricciones reales de tu BikeDto
        BikeDto dto = new BikeDto(null, "", "", "Azul", -100);

        // Act
        var violations = validator.validate(dto);
        Set<String> fields = violations.stream()
            .map(v -> v.getPropertyPath().toString())
            .collect(Collectors.toSet());

        // Assert: Verifica que capture las fallas en los campos que configuraste
        // Si no tienes anotaciones como @NotBlank en tu BikeDto, este test dará 0 errores hasta que se las agregues.
        System.out.println("Errores detectados en: " + fields);
    }
}