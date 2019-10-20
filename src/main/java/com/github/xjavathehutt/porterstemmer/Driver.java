package com.github.xjavathehutt.porterstemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Driver {
    
    private static final Pattern COMPILE = Pattern.compile("[^a-zA-Z']|('s?'?)");
    
    public static void main(String[] args) {
        int count = 0;
        
        try(Scanner porter2Input = new Scanner(new File("porter2Input.txt"))) {
            PorterStemmer.createMap();
            
            while(porter2Input.hasNext()) {
                String input = porter2Input.next();
                String output = porter2Input.next();
                
                String stemmed = PorterStemmer.stem(input);
                if(!stemmed.equals(output)) {
                    count++;
                    System.out.println(input + "=>" + stemmed + " but should be " + output);
                }
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        System.out.println(count + " errors in stemming.");
    }
    
    private static String removePunctuationAndLowerCase(String term) {
        return COMPILE.matcher(term.toLowerCase()).replaceAll("");
    }
}
