package com.github.hamzamemon.porterstemmer.stemming;

import com.github.hamzamemon.porterstemmer.constants.PorterStemmerConstants;
import com.github.hamzamemon.porterstemmer.suffixes.Step2Suffixes;
import com.github.hamzamemon.porterstemmer.suffixes.Step3Suffixes;
import com.github.hamzamemon.porterstemmer.suffixes.Step4Suffixes;
import com.github.hamzamemon.porterstemmer.util.WordMethods;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class does the Porter Stemmer algorithm (http://snowball.tartarus.org/algorithms/english/stemmer.html)
 */
public final class PorterStemmer {

    private static final Step2Suffixes[] STEP_2_SUFFIXES = Step2Suffixes.values();
    private static final Step3Suffixes[] STEP_3_SUFFIXES = Step3Suffixes.values();
    private static final Step4Suffixes[] STEP_4_SUFFIXES = Step4Suffixes.values();
    private static final Pattern VOWEL_NOT_IMMEDIATELY_BEFORE_S = Pattern.compile("^.*[" + PorterStemmerConstants.VOWELS + "].+s$");
    private static Map<String, String> stems = new HashMap<>();

    /**
     * R1 is the substring after the first consonant following a vowel
     *
     * @param term the word
     * @return the index
     */
    private static int getStartIndexOfR1(StringBuilder term) {
        String termS = term.toString();

        if (termS.startsWith("gener") || termS.startsWith("arsen")) {
            return 5;
        }
        if (termS.startsWith("commun")) {
            return 6;
        }

        return getIndexOfVowelAfterConsonant(term, 0);
    }

    /**
     * R2 is the substring after the first consonant following a vowel in R1
     *
     * @param term the word
     * @return the index
     */
    private static int getStartIndexOfR2(StringBuilder term, int r1) {
        return getIndexOfVowelAfterConsonant(term, r1);
    }

    /**
     * Gets index of vowel after consonant
     *
     * @param term  the term
     * @param start the starting position (i)
     * @return the index
     */
    private static int getIndexOfVowelAfterConsonant(StringBuilder term, int start) {
        String termS = term.toString();
        int i = start;
        int length = term.length();
        while (i < length && PorterStemmerConstants.VOWELS.indexOf(termS.charAt(i)) < 0) {
            i++;
        }
        while (i < length && PorterStemmerConstants.VOWELS.indexOf(termS.charAt(i)) >= 0) {
            i++;
        }

        return i;
    }

    /**
     * Performs the algorithm steps
     *
     * @param termS the word
     * @return the stemmed word
     */
    private static String makeStem(String termS) {
        if (termS.charAt(0) == '\'') {
            termS = termS.substring(1);
        }
        if (PorterStemmerConstants.EXCEPTIONAL_FORMS.containsKey(termS)) {
            return PorterStemmerConstants.EXCEPTIONAL_FORMS.get(termS);
        }

        StringBuilder term = new StringBuilder(termS);
        WordMethods.setCapitalYs(term);
        int r1 = getStartIndexOfR1(term);
        int r2 = getStartIndexOfR2(term, r1);
        doStep0(term);
        if (term.toString().isEmpty()) {
            return "";
        }

        doStep1a(term);

        String termToString = term.toString();

        if (PorterStemmerConstants.EXCEPTIONAL_FORMS_AFTER_STEP_1A.contains(termToString)) {
            return termToString;
        }

        doStep1bc(term, r1);
        doStep1c(term);
        doStep2To4(term, r1, r2);
        doStep5(term, r1, r2);

        termToString = term.toString();
        return termToString.replace("Y", "y");
    }

    /**
     * Does the stemming helper method, determines if the word has been stemmed before
     *
     * @param termS the word
     * @return the stemmed word
     */
    public static String stem(String termS) {
        if (termS.length() <= 2) {
            return termS;
        }

        String stem;

        if (stems.containsKey(termS)) {
            stem = stems.get(termS);
        } else {
            stem = makeStem(termS);
            stems.put(termS, stem);
        }

        return stem;
    }

    /**
     * Handles suffixes of "'", "'s" and "'s'"
     *
     * @param term the word
     */
    private static void doStep0(StringBuilder term) {
        String termS = term.toString();

        if (termS.endsWith("'s'")) {
            // *'s' -> *
            term.delete(term.length() - 3, term.length());
        } else if (termS.endsWith("'s")) {
            // *'s -> *
            term.delete(term.length() - 2, term.length());
        } else if (termS.charAt(termS.length() - 1) == '\'') {
            // *' -> *
            term.deleteCharAt(term.length() - 1);
        }
    }

    /**
     * Handles "s" suffixes
     *
     * @param term the word
     */
    private static void doStep1a(StringBuilder term) {
        String termS = term.toString();

        if (termS.endsWith("sses")) {
            // *sses -> *ss
            term.delete(term.length() - 2, term.length());
        } else if (termS.endsWith("ies") || termS.endsWith("ied")) {
            if (termS.length() >= 5) {
                // **ies/d -> **i
                term.delete(term.length() - 2, term.length());
            } else {
                // *ies/d -> *ie
                term.deleteCharAt(term.length() - 1);
            }
        } else if (termS.endsWith("ss") || termS.endsWith("us")) {
            // *ss -> *ss
        } else if (termS.charAt(termS.length() - 1) == 's') {
            // *s -> *
            Matcher matcher = VOWEL_NOT_IMMEDIATELY_BEFORE_S.matcher(termS);
            if (matcher.find()) {
                term.deleteCharAt(term.length() - 1);
            }
        }
    }

    /**
     * Handles suffixes of "ed" and "ing"
     *
     * @param term the word
     * @param r1   the R1 region
     */
    private static void doStep1bc(StringBuilder term, int r1) {
        String termS = term.toString();
        int length = term.length();

        if (termS.endsWith("eedly")) {
            if (term.length() - 5 >= r1) {
                // *eedly -> *e
                term.delete(length - 3, length);
            }
        } else if (termS.endsWith("eed")) {
            if (term.length() - 3 >= r1) {
                // *eed -> *ee
                term.deleteCharAt(length - 1);
            }
        } else {
            for (String suffix : PorterStemmerConstants.STEP_1B_SUFFIXES) {
                if (termS.endsWith(suffix)) {
                    String prefix = termS.substring(0, termS.length() - suffix.length());
                    if (WordMethods.getLetterTypes(prefix).contains(String.valueOf(PorterStemmerConstants.VOWEL))) {
                        term.delete(termS.length() - suffix.length(), term.length());
                        step1BPart2(term);
                        break;
                    }
                }
            }
        }

        if (term.charAt(term.length() - 1) == 'y' || term.charAt(term.length() - 1) == 'Y') {
            if (term.length() >= 3 && PorterStemmerConstants.VOWELS.indexOf(term.charAt(term.length() - 2)) < 0) {
                term.replace(term.length() - 1, term.length(), "i");
            }
        }
    }

    /**
     * Handles second part of Step 1b
     *
     * @param term the word
     */
    private static void step1BPart2(StringBuilder term) {
        String termS = term.toString();

        if (termS.endsWith("at") || termS.endsWith("bl") || termS.endsWith("iz")) {
            // *at -> *ate, *bl -> *ble, *iz -> *ize
            term.append('e');
        } else if (WordMethods.endsWithDouble(termS)) {
            // i.e. acquitt -> acquit
            term.deleteCharAt(term.length() - 1);
        } else if (WordMethods.isShort(termS)) {
            // i.e. bak -> bake
            term.append('e');
        }
    }

    /**
     * Replaces "y" with "i" if necessary
     *
     * @param term the word
     */
    private static void doStep1c(StringBuilder term) {
        if (term.charAt(term.length() - 1) == 'y' || term.charAt(term.length() - 1) == 'Y') {
            if (term.length() >= 3 && PorterStemmerConstants.VOWELS.indexOf(term.charAt(term.length() - 2)) < 0) {
                term.replace(term.length() - 1, term.length(), "i");
            }
        }
    }

    /**
     * Replaces suffixes with stemmed suffix using enums
     *
     * @param term the word
     * @param r1   the R1 region
     * @param r2   the R2 region
     */
    private static void doStep2To4(StringBuilder term, int r1, int r2) {
        // Step 2
        String termS = term.toString();
        for (Step2Suffixes step2Suffix : STEP_2_SUFFIXES) {
            String suffix = step2Suffix.toString();
            if (termS.endsWith(suffix)) {
                if (termS.length() - suffix.length() >= r1) {
                    if ("li".equals(suffix)) {
                        char liEnding = termS.charAt(termS.length() - 3);
                        if ("cdeghkmnrt".indexOf(liEnding) >= 0) {
                            removeSuffix(term, suffix, step2Suffix.getSuffix());
                        }
                    } else if ("ogi".equals(suffix)) {
                        char ogiEnding = termS.charAt(termS.length() - 4);
                        if (ogiEnding == 'l') {
                            removeSuffix(term, suffix, step2Suffix.getSuffix());
                        }
                    } else {
                        removeSuffix(term, suffix, step2Suffix.getSuffix());
                    }
                }

                break;
            }
        }

        // Step 3
        termS = term.toString();
        if (termS.endsWith("ative")) {
            if (termS.length() - 5 >= r2) {
                term.replace(term.length() - 5, term.length(), "");
                termS = term.toString();
            }
        }
        for (Step3Suffixes step3Suffix : STEP_3_SUFFIXES) {
            String suffix = step3Suffix.toString();
            if (termS.endsWith(suffix)) {
                if (termS.length() - suffix.length() >= r1) {
                    removeSuffix(term, suffix, step3Suffix.getSuffix());
                    break;
                }
            }
        }

        // Step 4
        termS = term.toString();

        if (termS.endsWith("sion") || termS.endsWith("tion")) {
            if (termS.length() - 3 >= r2) {
                term.delete(term.length() - 3, term.length());
            }
        } else {
            for (Step4Suffixes step4Suffix : STEP_4_SUFFIXES) {
                String suffix = step4Suffix.toString();
                if (termS.endsWith(suffix)) {
                    if (termS.length() - suffix.length() >= r2) {
                        String prefix = termS.substring(0, termS.length() - suffix.length());
                        if (WordMethods.getMeasure(prefix) > 1) {
                            term.replace(prefix.length(), term.length(), step4Suffix.getSuffix());
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Remove suffix for Steps 2-4
     *
     * @param term        the word
     * @param suffix      the suffix to remove
     * @param replacement what to replace the suffix with
     */
    private static void removeSuffix(StringBuilder term, String suffix, String replacement) {
        String prefix = term.substring(0, term.length() - suffix.length());
        if (WordMethods.getMeasure(prefix) > 0) {
            int lastIndex = prefix.length();
            term.replace(lastIndex, term.length(), replacement);
        }
    }

    /**
     * Remove ending "e" and "l" if necessary
     *
     * @param term the word
     * @param r1   the R1 region
     * @param r2   the R2 region
     */
    private static void doStep5(StringBuilder term, int r1, int r2) {
        String termS = term.toString();

        if (termS.charAt(termS.length() - 1) == 'e') {
            if (termS.length() - 1 >= r2) {
                term.deleteCharAt(term.length() - 1);
            } else {
                String withoutE = termS.substring(0, termS.length() - 1);
                if (termS.length() - 1 >= r1 && !WordMethods.endsWithShortSyllable(withoutE)) {
                    term.deleteCharAt(term.length() - 1);
                }
            }
        } else if (termS.endsWith("ll") && termS.length() - 1 >= r2) {
            term.deleteCharAt(term.length() - 1);
        }
    }

    /**
     * Constructor for PorterStemmer
     */
    private PorterStemmer() {
    }
}
