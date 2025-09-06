package org.qwics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IbanCheckerTest {

    private final IbanChecker ibanChecker = new IbanChecker();

    @Test
    void testValidateIban_ValidIban() {
        String validIban = "DE89370400440532013000"; // Gültige IBAN
        IbanChecker.IbanValidationResult result = ibanChecker.validateIban(validIban);

        assertTrue(result.isValid());
        assertEquals("IBAN ist gültig", result.getReason());
    }

    @Test
    void testValidateIban_InvalidLength() {
        String invalidIban = "DE89"; // Zu kurze IBAN
        IbanChecker.IbanValidationResult result = ibanChecker.validateIban(invalidIban);

        assertFalse(result.isValid());
        assertEquals("Ungültige IBAN-Länge", result.getReason());
    }

    @Test
    void testValidateIban_InvalidCountryCode() {
        String invalidIban = "X289370400440532013000"; // Ungültiger Länder-Code
        IbanChecker.IbanValidationResult result = ibanChecker.validateIban(invalidIban);

        assertFalse(result.isValid());
        assertEquals("Ungültiger Länder-Code", result.getReason());
    }

    @Test
    void testValidateIban_InvalidCheckDigits() {
        String invalidIban = "DE00370400440532013000"; // Ungültige Prüfziffer
        IbanChecker.IbanValidationResult result = ibanChecker.validateIban(invalidIban);

        assertFalse(result.isValid());
        assertEquals("IBAN-Prüfziffer ist ungültig", result.getReason());
    }

    @Test
    void testValidateIban_NullIban() {
        IbanChecker.IbanValidationResult result = ibanChecker.validateIban(null);

        assertFalse(result.isValid());
        assertEquals("Ungültige IBAN-Länge", result.getReason());
    }
    
    @Test
	void testValidateIban_InvalidCharacters() {
	    String invalidIban = "DE89-3704-0044-0532/0130-00"; // IBAN with dashes
	    IbanChecker.IbanValidationResult result = ibanChecker.validateIban(invalidIban);
	
	    assertFalse(result.isValid());
	    assertEquals("IBAN-Prüfziffer ist ungültig", result.getReason());
	}
    
    @Test
	void testValidateIban_EmptyIban() {
		String emptyIban = ""; // Leere IBAN
		IbanChecker.IbanValidationResult result = ibanChecker.validateIban(emptyIban);

		assertFalse(result.isValid());
		assertEquals("Ungültige IBAN-Länge", result.getReason());
	}

}