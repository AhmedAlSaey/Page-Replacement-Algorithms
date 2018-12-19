/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osassignment2;

import java.util.ArrayList;

/**
 *
 * @author Ahmed
 */
public class OSAssignment2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int referenceString[] = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2};
        ReplacmentAlgorithms x = new ReplacmentAlgorithms(referenceString,3);
        x.printReferenceString();
        System.out.println(x.LFU());
        x.printPageFrames();
        x.resetPageFrames();
//        int LRUIndex = 0;
//        int maxSteps = -1;
//        ArrayList<Integer> lru = new ArrayList<Integer>();
//        lru.add(7);
//        lru.add(0);
//        lru.add(1);
//        lru.add(2);
//        lru.add(0);
//        lru.add(3);
//        lru.add(0);
//        lru.add(4);
//        int pageFrames[] = {4,0,3};
//        for (int k = 0; k < pageFrames.length; k++) {
//            int steps = 0;
//            for (int p = lru.size() - 1; p >= 0; p--) {
//                if (lru.get(p) != pageFrames[k]) {
//                    steps++;
//                    if (steps > maxSteps) {
//                    LRUIndex = k;
//                    maxSteps = steps;
//                    }
//                }
//                else{
//                    if (steps > maxSteps) {
//                    LRUIndex = k;
//                    maxSteps = steps;
//                    }
//                    break;
//                }
//
//            }
//        }
//        System.out.println(LRUIndex);
    }
    
}
