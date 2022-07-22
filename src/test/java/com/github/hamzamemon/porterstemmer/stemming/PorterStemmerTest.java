package com.github.hamzamemon.porterstemmer.stemming;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PorterStemmerTest {
    
    @Test
    public void validateStems() {
        int count = 0;
        
        try (Scanner porter2Input = new Scanner(new File("./src/test/resources/porter2Input.txt"))) {
            while (porter2Input.hasNext()) {
                String input = porter2Input.next();
                String output = porter2Input.next();
                
                String stemmed = PorterStemmer.stem(input);
                if (!stemmed.equals(output)) {
                    count++;
                    System.out.println(input + "=>" + stemmed + " but should be " + output);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        assertEquals(0, count);
    }
    
    @Test
    public void test_EmptyStemmedWord() {
        assertEquals("", PorterStemmer.stem("''s"));
        assertEquals("", PorterStemmer.stem("''s'"));
    }
    
    @Test
    public void test_stem_MapAlreadyContainsStem() {
        String stemmed = PorterStemmer.stem("generation");
        
        assertEquals(stemmed, PorterStemmer.stem("generation"));
    }
}
