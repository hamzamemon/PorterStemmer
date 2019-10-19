import suffixes.Step2Suffixes;
import suffixes.Step3Suffixes;
import suffixes.Step4Suffixes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PorterStemmer {
    
    private static final HashSet<String> EXCEPTIONAL_FORMS_AFTER_STEP_1A = new HashSet<>(8);
    private static final Pattern VOWEL_NOT_IMMEDIATELY_BEFORE_S = Pattern.compile("^.*[aeiouy].+s$");
    private static HashMap<String, String> stems = new HashMap<>();
    private static HashMap<String, String> exceptionalForms = new HashMap<>(19);
    
    private PorterStemmer() {
    }
    
    public static void createMap() {
        String[] keys = {"skis", "skies", "dying", "lying", "tying", "idly", "gently", "ugly", "early", "only",
                "singly", "sky", "news", "howe", "atlas", "cosmos", "bias", "andes", "communing"};
        String[] values = {"ski", "sky", "die", "lie", "tie", "idl", "gentl", "ugli", "earli", "onli", "singl",
                "sky", "news", "howe", "atlas", "cosmos", "bias", "andes", "commune"};
        for(int i = 0, length = keys.length; i < length; i++) {
            exceptionalForms.put(keys[i], values[i]);
        }
        
        String[] EXCEPTIONAL_FORMS_AFTER_STEP_1A_ARRAY = {"inning", "outing", "canning", "herring", "earring",
                "proceed", "exceed", "succeed"};
        EXCEPTIONAL_FORMS_AFTER_STEP_1A.addAll(Arrays.asList(EXCEPTIONAL_FORMS_AFTER_STEP_1A_ARRAY));
    }
    
    private static int getStartIndexOfR1(StringBuilder term) {
        String termS = term.toString();
        
        if(termS.startsWith("gener") || termS.startsWith("arsen")) {
            return 5;
        }
        if(termS.startsWith("commun")) {
            return 6;
        }
        
        int i = 0;
        while(i < termS.length() && "aeiouy".indexOf(termS.charAt(i)) < 0) {
            i++;
        }
        while(i < termS.length() && "aeiouy".indexOf(termS.charAt(i)) >= 0) {
            i++;
        }
        
        return i;
    }
    
    private static int getStartIndexOfR2(StringBuilder term, int r1) {
        String termS = term.toString();
        
        int i = r1;
        while(i < termS.length() && "aeiouy".indexOf(termS.charAt(i)) < 0) {
            i++;
        }
        while(i < termS.length() && "aeiouy".indexOf(termS.charAt(i)) >= 0) {
            i++;
        }
        
        return i;
    }
    
    private static String makeStem(String termS) {
        StringBuilder term = new StringBuilder(termS);
        if(term.charAt(0) == '\'') {
            term = term.deleteCharAt(0);
        }
        String termToString = term.toString();
        if(exceptionalForms.containsKey(termToString)) {
            return exceptionalForms.get(termToString);
        }
        
        WordMethods.setCapitalYs(term);
        int r1 = getStartIndexOfR1(term);
        int r2 = getStartIndexOfR2(term, r1);
        doStep0(term);
        doStep1a(term);
        
        termToString = term.toString();
        if(EXCEPTIONAL_FORMS_AFTER_STEP_1A.contains(termToString)) {
            return termToString;
        }
        
        doStep1bc(term, r1);
        doStep2To4(term, r1, r2);
        doStep5(term, r1, r2);
        
        termToString = term.toString();
        return termToString.replace("Y", "y");
    }
    
    public static String stem(String termS) {
        if(termS.length() <= 2) {
            return termS;
        }
        
        String stem;
        
        if(stems.containsKey(termS)) {
            stem = stems.get(termS);
        }
        else {
            stem = makeStem(termS);
            stems.put(termS, stem);
        }
        
        return stem;
    }
    
    private static void doStep0(StringBuilder term) {
        String termS = term.toString();
        
        if(termS.endsWith("'s'")) {
            term = term.delete(term.length() - 3, term.length());
        }
        else if(termS.endsWith("'s")) {
            term = term.delete(term.length() - 2, term.length());
        }
        else if(termS.charAt(termS.length() - 1) == '\'') {
            term = term.deleteCharAt(term.length() - 1);
        }
    }
    
    private static void doStep1a(StringBuilder term) {
        String termS = term.toString();
        
        if(termS.endsWith("sses")) {
            // *sses -> *ss
            term = term.delete(term.length() - 2, term.length());
        }
        else if(termS.endsWith("ies") || termS.endsWith("ied")) {
            if(termS.length() >= 5) {
                term = term.delete(term.length() - 2, term.length());
            }
            else {
                term = term.deleteCharAt(term.length() - 1);
            }
        }
        else if(termS.endsWith("ss") || termS.endsWith("us")) {
            // *ss -> *ss
        }
        else if(termS.charAt(termS.length() - 1) == 's') {
            // *s -> *
            Matcher matcher = VOWEL_NOT_IMMEDIATELY_BEFORE_S.matcher(termS);
            if(matcher.find()) {
                term = term.deleteCharAt(term.length() - 1);
            }
        }
    }
    
    private static void doStep1bc(StringBuilder term, int r1) {
        String termS = term.toString();
        int length = term.length();
        
        if(termS.endsWith("eedly")) {
            if(term.length() - 5 >= r1) {
                term = term.delete(length - 3, length);
            }
        }
        
        else if(termS.endsWith("eed")) {
            if(term.length() - 3 >= r1) {
                term = term.deleteCharAt(length - 1);
            }
        }
        
        else {
            String[] suffixes = {"ed", "edly", "ing", "ingly"};
            for(String suffix : suffixes) {
                if(termS.endsWith(suffix)) {
                    String prefix = termS.substring(0, termS.length() - suffix.length());
                    if(getLetterTypes(prefix).contains("V")) {
                        term = term.delete(termS.length() - suffix.length(), term.length());
                        term = step1BPart2(term);
                        break;
                    }
                }
            }
        }
        
        if(term.charAt(term.length() - 1) == 'y' || term.charAt(term.length() - 1) == 'Y') {
            if(term.length() >= 3 && "aeiouy".indexOf(term.charAt(term.length() - 2)) < 0) {
                term = term.replace(term.length() - 1, term.length(), "i");
            }
        }
    }
    
    private static StringBuilder step1BPart2(StringBuilder term) {
        String termS = term.toString();
        
        if(termS.endsWith("at") || termS.endsWith("bl") || termS.endsWith("iz")) {
            term = term.append('e');
        }
        else if(WordMethods.endsWithDouble(term)) {
            term = term.deleteCharAt(term.length() - 1);
        }
        else if(isShort(termS)) {
            term = term.append('e');
        }
        
        return term;
    }
    
    private static void doStep2To4(StringBuilder term, int r1, int r2) {
        // Step 2
        Step2Suffixes[] step2Suffixes = Step2Suffixes.values();
        
        String termS = term.toString();
        for(Step2Suffixes step2Suffix : step2Suffixes) {
            String suffix = step2Suffix.toString();
            if(termS.endsWith(suffix)) {
                if(termS.length() - suffix.length() >= r1) {
                    if("li".equals(suffix)) {
                        char liEnding = termS.charAt(termS.length() - 3);
                        if(liEnding == 'c' || liEnding == 'd' || liEnding == 'e' || liEnding == 'g' || liEnding == 'h' ||
                                liEnding == 'k' || liEnding == 'm' || liEnding == 'n' || liEnding == 'r' || liEnding == 't') {
                            removeSuffix(term, suffix, step2Suffix.getSuffix());
                        }
                    }
                    else if("ogi".equals(suffix)) {
                        char ogiEnding = termS.charAt(termS.length() - 4);
                        if(ogiEnding == 'l') {
                            removeSuffix(term, suffix, step2Suffix.getSuffix());
                        }
                    }
                    else {
                        removeSuffix(term, suffix, step2Suffix.getSuffix());
                    }
                }
                
                break;
            }
        }
        
        // Step 3
        Step3Suffixes[] step3Suffixes = Step3Suffixes.values();
        
        termS = term.toString();
        if(termS.endsWith("ative")) {
            if(termS.length() - 5 >= r2) {
                term = term.replace(term.length() - 5, term.length(), "");
                termS = term.toString();
            }
        }
        for(Step3Suffixes step3Suffix : step3Suffixes) {
            String suffix = step3Suffix.toString();
            if(termS.endsWith(suffix)) {
                if(termS.length() - suffix.length() >= r1) {
                    removeSuffix(term, suffix, step3Suffix.getSuffix());
                    break;
                }
            }
        }
        
        // Step 4
        Step4Suffixes[] step4Suffixes = Step4Suffixes.values();
        termS = term.toString();
        
        if(termS.endsWith("sion") || termS.endsWith("tion")) {
            if(termS.length() - 3 >= r2) {
                term = term.delete(term.length() - 3, term.length());
            }
        }
        
        else {
            for(Step4Suffixes step4Suffix : step4Suffixes) {
                String suffix = step4Suffix.toString();
                if(termS.endsWith(suffix)) {
                    if(termS.length() - suffix.length() >= r2) {
                        String prefix = termS.substring(0, termS.length() - suffix.length());
                        if(getMeasure(prefix) > 1) {
                            term = term.replace(prefix.length(), term.length(), step4Suffix.getSuffix());
                        }
                    }
                    break;
                }
            }
        }
    }
    
    private static void doStep5(StringBuilder term, int r1, int r2) {
        String termS = term.toString();
        
        if(termS.charAt(termS.length() - 1) == 'e') {
            if(termS.length() - 1 >= r2) {
                term = term.deleteCharAt(term.length() - 1);
            }
            else {
                String withoutE = termS.substring(0, termS.length() - 1);
                if(termS.length() - 1 >= r1 && !endsWithShortSyllable(withoutE)) {
                    term = term.deleteCharAt(term.length() - 1);
                }
            }
        }
        
        else if(termS.endsWith("ll") && termS.length() - 1 >= r2) {
            term = term.deleteCharAt(term.length() - 1);
        }
    }
    
    public static boolean isShort(String termS) {
        return endsWithShortSyllable(termS) && getMeasure(termS) == 1;
    }
    
    public static boolean endsWithShortSyllable(String termS) {
        if(termS.length() == 2) {
            return "aeiouy".indexOf(termS.charAt(0)) >= 0 && "aeiouy".indexOf(termS.charAt(1)) < 0;
        }
        char last = termS.charAt(termS.length() - 1);
        if(last == 'w' || last == 'x' || last == 'Y') {
            return false;
        }
        char secondLast = termS.charAt(termS.length() - 2);
        char thirdLast = termS.charAt(termS.length() - 3);
        
        return !WordMethods.isVowel(thirdLast) && WordMethods.isVowel(secondLast) && !WordMethods.isVowel(last);
    }
    
    private static void removeSuffix(StringBuilder term, String suffix, String replacement) {
        String prefix = term.substring(0, term.length() - suffix.length());
        if(getMeasure(prefix) > 0) {
            int lastIndex = prefix.length();
            term = term.replace(lastIndex, term.length(), replacement);
        }
    }
    
    private static String getLetterTypes(String word) {
        StringBuilder letterTypes = new StringBuilder(word.length());
        for(int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            char letterType = WordMethods.getLetterType(letter);
            if(letterTypes.length() == 0 || letterTypes.charAt(letterTypes.length() - 1) != letterType) {
                letterTypes.append(letterType);
            }
        }
        
        return letterTypes.toString();
    }
    
    private static int getMeasure(String word) {
        String letterTypes = getLetterTypes(word);
        if(letterTypes.length() < 2) {
            return 0;
        }
        if(letterTypes.charAt(0) == 'C') {
            return (letterTypes.length() - 1) >> 1;
        }
        return letterTypes.length() >> 1;
    }
}
