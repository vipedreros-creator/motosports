package com.motosport.bff.shared.utils;

import java.util.Locale;

public final class BikeTextUtils {

    private BikeTextUtils() {
    }

    public static boolean containsIgnoreCase(String source, String expected) {
        if (source == null || expected == null) {
            return false;
        }
        return normalize(source).contains(normalize(expected));
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}