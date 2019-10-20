package com.xjavathehutt.porterstemmer.suffixes;

/**
 * Class to convert a suffix to Porter Stemmed suffix
 */
public enum Step3Suffixes {
    
    // 7 letters
    ATIONAL("ate"),
    
    // 6 letters
    TIONAL("tion"),
    
    // 5 letters
    ALIZE("al"),
    ICATE("ic"),
    ICITI("ic"),
    
    // 4 letters
    ICAL("ic"),
    NESS(""),
    
    // 3 letters
    FUL("");
    
    final String suffix;
    
    Step3Suffixes(String suffix) {
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
