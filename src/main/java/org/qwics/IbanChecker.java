package org.qwics;

import lombok.Getter;

import java.math.BigInteger;

public class IbanChecker {
    public static class IbanValidationResult {
        private final boolean isValid;
        @Getter
        private final String reason;

        public IbanValidationResult(boolean isValid, String reason) {
            this.isValid = isValid;
            this.reason = reason;
        }

        public boolean isValid() {
            return isValid;
        }

    }

    public IbanChecker.IbanValidationResult validateIban(String iban) {
        if (iban == null || iban.length() < 15 || iban.length() > 34) {
            return new IbanChecker.IbanValidationResult(false, "Ungültige IBAN-Länge");
        }

        // Schritt 1: Länder-Code und Prüfziffer extrahieren
        String countryCode = iban.substring(0, 2);
        String checkDigits = iban.substring(2, 4);
        String bban = iban.substring(4);

        // Schritt 2: Länder-Code in numerische Werte umwandeln
        String numericCountryCode = convertCountryCodeToNumeric(countryCode);

        if (numericCountryCode == null) {
            return new IbanChecker.IbanValidationResult(false, "Ungültiger Länder-Code");
        }

        // Schritt 3: IBAN umstellen und in numerischen String umwandeln
        String rearrangedIban = bban + numericCountryCode + checkDigits;

        // Schritt 4: Luhn-Algorithmus (Modulo 97)
        if (isValidLuhn(rearrangedIban)) {
            return new IbanChecker.IbanValidationResult(true, "IBAN ist gültig");
        } else {
            return new IbanChecker.IbanValidationResult(false, "IBAN-Prüfziffer ist ungültig");
        }
    }

    private String convertCountryCodeToNumeric(String countryCode) {
        StringBuilder numericCode = new StringBuilder();
        for (char c : countryCode.toCharArray()) {
            if (Character.isLetter(c)) {
                numericCode.append((int) c - 55); // A=10, B=11, ..., Z=35
            } else {
                return null; // Ungültiger Länder-Code
            }
        }
        return numericCode.toString();
    }

    private boolean isValidLuhn(String numericIban) {
        try {
            BigInteger ibanNumber = new BigInteger(numericIban);
            return ibanNumber.mod(BigInteger.valueOf(97)).intValue() == 1;
        } catch (NumberFormatException e) {
            return false; // Ungültige numerische IBAN
        }
    }

    public static void main(String[] args) {
        IbanChecker checker = new IbanChecker();
        String iban = "DE89370400440532013000"; // Beispiel-IBAN

        IbanChecker.IbanValidationResult result = checker.validateIban(iban);
        System.out.println("IBAN: " + iban);
        System.out.println("Gültig: " + result.isValid());
        System.out.println("Grund: " + result.getReason());
    }

}
