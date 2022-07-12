package com.github.hamzamemon.porterstemmer.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Constants for the Porter Stemmer algorithm
 */
public final class PorterStemmerConstants {

    public static final char VOWEL = 'V';
    public static final char CONSONANT = 'C';
    public static final Pattern DOUBLES = Pattern.compile(".*(bb|dd|ff|gg|mm|nn|pp|rr|tt)$");

    public static final String VOWELS = "aeiouy";

    public static final Map<String, String> EXCEPTIONAL_FORMS = new HashMap<>(19);
    public static final Set<String> EXCEPTIONAL_FORMS_AFTER_STEP_1A = new HashSet<>(8);

    public static final String[] STEP_1B_SUFFIXES = {"ed", "edly", "ing", "ingly"};

    static {
        String[] keys = {"skis", "skies", "dying", "lying", "tying", "idly", "gently", "ugly", "early", "only",
                "singly", "sky", "news", "howe", "atlas", "cosmos", "bias", "andes", "communing"};
        String[] values = {"ski", "sky", "die", "lie", "tie", "idl", "gentl", "ugli", "earli", "onli", "singl",
                "sky", "news", "howe", "atlas", "cosmos", "bias", "andes", "commune"};
        for (int i = 0, length = keys.length; i < length; i++) {
            EXCEPTIONAL_FORMS.put(keys[i], values[i]);
        }

        String[] EXCEPTIONAL_FORMS_AFTER_STEP_1A_ARRAY = {"inning", "outing", "canning", "herring", "earring",
                "proceed", "exceed", "succeed"};
        EXCEPTIONAL_FORMS_AFTER_STEP_1A.addAll(Arrays.asList(EXCEPTIONAL_FORMS_AFTER_STEP_1A_ARRAY));
    }

    /**
     * Constructor for PorterStemmerConstants
     */
    private PorterStemmerConstants() {
    }
}
