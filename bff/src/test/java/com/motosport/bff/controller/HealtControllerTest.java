package com.motosport.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

class HealthControllerTest {

    @Test
    void health_shouldReturnOkResponseEntity() {
        // Arrange: Instanciamos el controlador de tu proyecto
        HealthController healthController = new HealthController();

        // Act: Ejecutamos el método que revisa el estado del BFF
        ResponseEntity<Map<String, Object>> result = healthController.health();

        // Assert: Validamos que responda un 200 OK y que traiga contenido
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}