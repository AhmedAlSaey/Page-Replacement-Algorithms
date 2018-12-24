package PageReplacementAlgorithms;

import java.util.ArrayList;

/**
 *
 * @author Ahmed AlSaey
 */
public class Main {
    public static void main(String[] args) {
//        Uncomment below code for a fixed example (lecture example).
        int referenceString[] = {7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1};
        int numberOfPageFrames = 3;
        ReplacmentAlgorithms x = new ReplacmentAlgorithms(referenceString,numberOfPageFrames);
//        System.out.println("FIFO misses: " + x.FIFO());
//        System.out.println("Least frequently used misses: " + x.LFU());
//        System.out.println("Least recently used misses: " + x.LRU());
//        System.out.println("Optimal misses: " + x.optimal());
//        System.out.println("Second chance misses: " + x.secondChance());
        System.out.println("Enhance second chance misses: " + x.enhancedSecondChance());

//        --------------------------------------------

//        Uncomment below code for a random example.
//        ReplacmentAlgorithms x = new ReplacmentAlgorithms();
//        System.out.println("FIFO misses: " + x.FIFO());
//        System.out.println("Least frequently used misses: " + x.LFU());
//        System.out.println("Least recently used misses: " + x.LRU());
//        System.out.println("Optimal misses: " + x.optimal());
//        System.out.println("Second chance misses: " + x.secondChance());
//        System.out.println("Enhance second chance misses: " + x.enhancedSecondChance());
    }
    
}
