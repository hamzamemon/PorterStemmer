package com.github.hamzamemon.porterstemmer.util;

import com.github.hamzamemon.porterstemmer.constants.PorterStemmerConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Methods to handle the Porter Stemmer algorithm
 */
public final class WordMethods {
    
    private static Map<String, StringBuilder> mapCapitalYs = new HashMap<>();
    private static Map<String, String> stringToLetterTypes = new HashMap<>();
    
    /**
     * Determines if a letter is a vowel or not
     *
     * @param letter the letter
     * @return vowel or consonant
     */
    public static char getLetterType(char letter) {
        return PorterStemmerConstants.VOWELS.indexOf(letter) >= 0 ? PorterStemmerConstants.VOWEL : PorterStemmerConstants.CONSONANT;
    }
    
    /**
     * Determines is the letter is a vowel
     *
     * @param letter the letter
     * @return is vowel or not
     */
    private static boolean isVowel(char letter) {
        return getLetterType(letter) == PorterStemmerConstants.VOWEL;
    }
    
    /**
     * Determines if the term ends in a double consonant
     *
     * @param termS the term
     * @return if term ends with double or not
     */
    public static boolean endsWithDouble(String termS) {
        return PorterStemmerConstants.DOUBLES.matcher(termS.toLowerCase()).matches();
    }
    
    /**
     * Set "y" to "Y" if preceded by a consonant that's not the first letter or if first letter is "y"
     *
     * @param term the word
     */
    public static void setCapitalYs(StringBuilder term) {
        String termS = term.toString().toLowerCase();
        if (mapCapitalYs.containsKey(termS)) {
            term = mapCapitalYs.get(termS);
            return;
        }
        
        if (termS.contains("y")) {
            if (term.charAt(0) == 'y') {
                term = term.replace(0, 1, "Y");
            }
            for (int i = 1, length = term.length(); i < length; i++) {
                if (term.charAt(i) == 'y') {
                    if (isVowel(term.charAt(i - 1))) {
                        term = term.replace(i, i + 1, "Y");
                    }
                }
            }
        }
        
        mapCapitalYs.put(termS, term);
    }
    
    /**
     * Determines if a word is "short" - ends with short syllable and R1 is null
     *
     * @param termS the word
     * @return if word is short or not
     */
    public static boolean isShort(String termS) {
        return endsWithShortSyllable(termS) && getMeasure(termS) == 1;
    }
    
    /**
     * Determines if word is a short syllable:
     * Vowel followed by consonant (not "w", "x" or "Y" and preceded by consonant
     * OR
     * Vowel at the beginning of the word followed by consonant
     *
     * @param termS the word
     * @return if word is a short syllable
     */
    public static boolean endsWithShortSyllable(String termS) {
        if (termS.length() < 2) {
            // V, C
            return false;
        }
        
        if (termS.length() == 2) {
            // VC
            return PorterStemmerConstants.VOWELS.indexOf(termS.charAt(0)) >= 0 && PorterStemmerConstants.VOWELS.indexOf(termS.charAt(1)) < 0;
        }
        
        char last = termS.charAt(termS.length() - 1);
        if (last == 'w' || last == 'x' || last == 'Y') {
            // **w, **x, **Y
            return false;
        }
        char secondLast = termS.charAt(termS.length() - 2);
        char thirdLast = termS.charAt(termS.length() - 3);
        
        // *CVC
        return !isVowel(thirdLast) && isVowel(secondLast) && !isVowel(last);
    }
    
    /**
     * Convert word to Vs and Cs (vowel and consonant)
     *
     * @param word the word
     * @return the converted word
     */
    public static String getLetterTypes(String word) {
        if (stringToLetterTypes.containsKey(word)) {
            return stringToLetterTypes.get(word);
        }
        
        StringBuilder letterTypes = new StringBuilder(word.length());
        for (int i = 0, length = word.length(); i < length; i++) {
            char letter = word.charAt(i);
            char letterType = getLetterType(letter);
            if (letterTypes.isEmpty() || letterTypes.charAt(letterTypes.length() - 1) != letterType) {
                letterTypes.append(letterType);
            }
        }
        
        String letterTypesS = letterTypes.toString();
        stringToLetterTypes.put(word, letterTypesS);
        return letterTypesS;
    }
    
    /**
     * Number of CV pairs
     *
     * @param word the word
     * @return number of pairs
     */
    public static int getMeasure(String word) {
        String letterTypes = getLetterTypes(word);
        if (letterTypes.length() <= 1) {
            return 0;
        }
        if (letterTypes.charAt(0) == 'C') {
            return letterTypes.length() - 1 >> 1;
        }
        return letterTypes.length() >> 1;
    }
    
    /**
     * Constructor for WordMethods
     */
    private WordMethods() {
    }
}
