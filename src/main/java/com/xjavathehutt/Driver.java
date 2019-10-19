package com.xjavathehutt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Driver {
    
    private static final Pattern COMPILE = Pattern.compile("[^a-zA-Z']|('s?'?)");
    
    public static void main(String[] args) {
        int count = 0;
        
        try(Scanner scanner = new Scanner(new File("testInput.txt"))) {
            Scanner scanner2 = new Scanner(new File("testOutput.txt"));
            while(scanner.hasNextLine() && scanner2.hasNextLine()) {
                String input = scanner.nextLine();
                String output = scanner2.nextLine();
                
                String processedWord = removePunctuationAndLowerCase(input);
                String stem = PorterStemmer.stem(processedWord);
                
                if(!stem.equals(output)) {
                    System.out.println("input=" + input + " => " + stem + " should be " + output);
                    count++;
                }
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        System.out.println(count);
    }
    
    private static String removePunctuationAndLowerCase(String term) {
        return COMPILE.matcher(term.toLowerCase()).replaceAll("");
    }
}
