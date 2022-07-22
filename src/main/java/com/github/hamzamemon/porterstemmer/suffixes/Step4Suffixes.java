package com.github.hamzamemon.porterstemmer.suffixes;

/**
 * Class to convert a suffix to Porter Stemmed suffix
 */
public enum Step4Suffixes {
    
    // 5 letters
    EMENT(""),
    
    // 4 letters
    ANCE(""),
    ENCE(""),
    ABLE(""),
    IBLE(""),
    MENT(""),
    
    // 3 letters
    ANT(""),
    ENT(""),
    ISM(""),
    ATE(""),
    ITI(""),
    OUS(""),
    IVE(""),
    IZE(""),
    
    // 2 letters
    AL(""),
    ER(""),
    IC("");
    
    private final String suffix;
    
    Step4Suffixes(String suffix) {
        this.suffix = suffix;
    }
    
    /**
     * Gets the suffix
     *
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }
    
    /**
     * Output object as String
     *
     * @return the object as a String
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
