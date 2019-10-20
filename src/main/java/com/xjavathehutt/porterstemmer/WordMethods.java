package com.xjavathehutt.porterstemmer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Methods to handle the Porter Stemmer algorithm
 */
public final class WordMethods {
    
    private static final Pattern DOUBLES = Pattern.compile(".*(bb|dd|ff|gg|mm|nn|pp|rr|tt)$");
    private static Map<String, StringBuilder> mapCapitalYs = new HashMap<>();
    
    /**
     * Determines if a letter is a vowel or not, helper method
     *
     * @param letter the letter
     *
     * @return Vowel or consonant
     */
    public static char getLetterType(char letter) {
        switch(letter) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'y':
                return 'V';
            default:
                return 'C';
        }
    }
    
    /**
     * Determines is the letter is a vowel
     *
     * @param letter the letter
     *
     * @return is vowel or not
     */
    public static boolean isVowel(char letter) {
        return getLetterType(letter) == 'V';
    }
    
    /**
     * Determines if the term ends in a double consonant
     *
     * @param termS the term
     *
     * @return if term ends with double or not
     */
    public static boolean endsWithDouble(String termS) {
        return DOUBLES.matcher(termS).matches();
    }
    
    /**
     * Set "y" to "Y" if preceded by a consonant that's not the first letter or if first letter is "y"
     *
     * @param term the word
     */
    public static void setCapitalYs(StringBuilder term) {
        String termS = term.toString();
        if(mapCapitalYs.containsKey(termS)) {
            term = mapCapitalYs.get(termS);
            return;
        }
        
        if(termS.contains("y")) {
            if(term.charAt(0) == 'y') {
                term = term.replace(0, 1, "Y");
            }
            for(int i = 1, length = term.length(); i < length; i++) {
                if(term.charAt(i) == 'y') {
                    if(isVowel(term.charAt(i - 1))) {
                        term = term.replace(i, i + 1, "Y");
                    }
                }
            }
        }
        
        mapCapitalYs.put(termS, term);
    }
}
