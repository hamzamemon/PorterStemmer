package com.github.hamzamemon.porterstemmer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordMethodsTest {

    @Test
    public void test_getLetterType() {
        assertEquals('V', WordMethods.getLetterType('a'));
        assertEquals('C', WordMethods.getLetterType('b'));
    }

    @Test
    public void test_endsWithDouble() {
        assertTrue(WordMethods.endsWithDouble("putt"));
        assertTrue(WordMethods.endsWithDouble("puTT"));
        assertFalse(WordMethods.endsWithDouble("punt"));
    }

    @Test
    public void test_setCapitalYs() {
        StringBuilder input = new StringBuilder("yes");
        WordMethods.setCapitalYs(input);
        assertEquals("Yes", input.toString());

        input = new StringBuilder("buy");
        WordMethods.setCapitalYs(input);
        assertEquals("buY", input.toString());

        input = new StringBuilder("yes");
        WordMethods.setCapitalYs(input);
        assertEquals("yes", input.toString());
    }

    @Test
    public void test_isShort() {
        assertTrue(WordMethods.isShort("hat"));
        assertFalse(WordMethods.isShort("saw"));
    }

    @Test
    public void test_endsWithShortSyllable() {
        assertFalse(WordMethods.endsWithShortSyllable("a"));

        assertTrue(WordMethods.endsWithShortSyllable("at"));
        assertFalse(WordMethods.endsWithShortSyllable("ao"));
        assertFalse(WordMethods.endsWithShortSyllable("be"));
        assertFalse(WordMethods.endsWithShortSyllable("bq"));

        assertFalse(WordMethods.endsWithShortSyllable("saw"));
        assertFalse(WordMethods.endsWithShortSyllable("sax"));
        assertFalse(WordMethods.endsWithShortSyllable("say"));
        assertFalse(WordMethods.endsWithShortSyllable("saY"));
        assertTrue(WordMethods.endsWithShortSyllable("sap"));
    }

    @Test
    public void test_getLetterTypes() {
        assertEquals("CVC", WordMethods.getLetterTypes("hat"));
        assertEquals("CVC", WordMethods.getLetterTypes("hat"));
    }

    @Test
    public void test_getMeasure() {
        assertEquals(0, WordMethods.getMeasure("a"));

        assertEquals(1, WordMethods.getMeasure("at"));
        assertEquals(0, WordMethods.getMeasure("be"));

        assertEquals(2, WordMethods.getMeasure("between"));
        assertEquals(1, WordMethods.getMeasure("been"));
    }
}
