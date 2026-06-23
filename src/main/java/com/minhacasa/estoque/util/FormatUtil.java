package com.minhacasa.estoque.util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Helpers de formatacao (moeda BRL e datas no padrao pt-BR). */
public final class FormatUtil {

    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(PT_BR);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_FORMATTER_LONG =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", PT_BR);

    private FormatUtil() {
    }

    public static String currency(double value) {
        return CURRENCY_FORMAT.format(value);
    }

    public static String date(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

    public static String dateLong(LocalDate date) {
        if (date == null) {
            return "";
        }
        String texto = date.format(DATE_FORMATTER_LONG);
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    public static double parseDoubleOrZero(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        try {
            return Double.parseDouble(text.replace(".", "").replace(",", "."));
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(text.trim());
            } catch (NumberFormatException ex) {
                return 0;
            }
        }
    }

    public static int parseIntOrZero(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
