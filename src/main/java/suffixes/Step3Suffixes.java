package suffixes;

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
    
    public String getSuffix() {
        return suffix;
    }
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
