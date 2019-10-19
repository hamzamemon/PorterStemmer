import com.xjavathehutt.PorterStemmer;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class PorterStemmerTest {
    
    @Test
    public void testEndsWithShortSyllables() {
        String[] goodInputs = {"rap", "trap", "entrap"};
        String[] badInputs = {"uproot", "bestow", "disturb"};
        
        int goodCount = 0;
        for(String input : goodInputs) {
            if(PorterStemmer.endsWithShortSyllable(input)) {
                goodCount++;
            }
        }
        
        assertEquals(goodCount, 3);
        
        int badCount = 0;
        for(String input : badInputs) {
            if(!PorterStemmer.endsWithShortSyllable(input)) {
                badCount++;
            }
        }
        
        assertEquals(badCount, 3);
    }
    
    @Test
    public void testIsShort() {
        String[] goodInputs = {"bed", "shed", "shred"};
        String[] badInputs = {"bead", "embed", "beds"};
        
        int goodCount = 0;
        for(String input : goodInputs) {
            if(PorterStemmer.isShort(input)) {
                goodCount++;
            }
        }
        
        assertEquals(goodCount, 3);
        
        int badCount = 0;
        for(String input : badInputs) {
            if(!PorterStemmer.isShort(input)) {
                badCount++;
            }
        }
        
        assertEquals(badCount, 3);
    }
    
    @Test
    public void testPorter2Algorithm() {
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
        
        assertEquals(count, 0);
    }
    
}
