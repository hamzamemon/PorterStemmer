package com.xjavathehutt.suffixes;

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
    
    final String suffix;
    
    Step4Suffixes(String suffix) {
        this.suffix = suffix;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
