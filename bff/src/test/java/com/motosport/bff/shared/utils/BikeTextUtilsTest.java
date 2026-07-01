package com.motosport.bff.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BikeTextUtilsTest {

    @Test
    void normalize_shouldTrimAndLowerCase() {
        // Test puro: valida el comportamiento con espacios y mayúsculas
        String result = BikeTextUtils.normalize("   MoToSpOrT   ");

        assertEquals("motosport", result);
    }

    @Test
    void normalize_shouldReturnEmptyForNull() {
        String result = BikeTextUtils.normalize(null);

        assertEquals("", result);
    }

    @Test
    void containsIgnoreCase_shouldReturnTrueWhenMatch() {
        boolean result = BikeTextUtils.containsIgnoreCase("Tienda Suzuki Santiago", "sUzUkI");

        assertTrue(result);
    }

    @Test
    void containsIgnoreCase_shouldReturnFalseWhenAnyNull() {
        assertFalse(BikeTextUtils.containsIgnoreCase(null, "abc"));
        assertFalse(BikeTextUtils.containsIgnoreCase("abc", null));
    }
}