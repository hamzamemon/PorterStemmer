package com.xjavathehutt;

import java.util.regex.Pattern;

public final class WordMethods {
    
    private static final Pattern DOUBLES = Pattern.compile(".*(bb|dd|ff|gg|mm|nn|pp|rr|tt)$");
    
    private WordMethods() {
    }
    
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
    
    public static boolean isVowel(char letter) {
        return getLetterType(letter) == 'V';
    }
    
    public static boolean endsWithDouble(StringBuilder term) {
        String termS = term.toString();
        return DOUBLES.matcher(termS).matches();
    }
    
    public static void setCapitalYs(StringBuilder term) {
        if(term.toString().contains("y")) {
            if(term.charAt(0) == 'y') {
                term = term.replace(0, 1, "Y");
            }
            for(int i = 1; i < term.length(); i++) {
                if(term.charAt(i) == 'y') {
                    if(isVowel(term.charAt(i - 1))) {
                        term = term.replace(i, i + 1, "Y");
                    }
                }
            }
        }
    }
}
