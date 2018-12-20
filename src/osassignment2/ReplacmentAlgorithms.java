/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osassignment2;

import java.sql.Time;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ReplacmentAlgorithms {

    int referenceString[];
    int pageFrames[];

    public ReplacmentAlgorithms() {
        Random rand = new Random();
        referenceString = new int[rand.nextInt(81) + 20]; //Assume max size of reference string is 100 and minimum is less than the max possible size of reference string
        for (int i = 0; i < referenceString.length; i++) {
            referenceString[i] = rand.nextInt(100);
        }
        pageFrames = new int[rand.nextInt(20) + 1];
        Arrays.fill(pageFrames, -1);

    }

    public ReplacmentAlgorithms(int referenceString[], int pageFramesSize) {
        this.referenceString = referenceString;
        this.pageFrames = new int[pageFramesSize];
        Arrays.fill(pageFrames, -1);
    }

    public void printReferenceString() {
        String temp = "Reference string is: [";
        for (int i = 0; i < referenceString.length; i++) {
            temp += referenceString[i] + ",";
        }
        temp = temp.substring(0, temp.length() - 1);
        temp += "]";
        System.out.println(temp);
    }

    public void printPageFrames() {
        String temp = "Page frames filled with: [";
        for (int i = 0; i < pageFrames.length; i++) {
            temp += pageFrames[i] + ",";
        }
        temp = temp.substring(0, temp.length() - 1);
        temp += "]";
        System.out.println(temp);
    }

    public void generateRandomReferenceString() {
        Random rand = new Random();
        for (int i = 0; i < referenceString.length; i++) {
            referenceString[i] = rand.nextInt(100);
        }
    }

    public void resetPageFrames() {
        Random rand = new Random();
        pageFrames = new int[rand.nextInt(20) + 1];
        Arrays.fill(pageFrames, -1);
    }

    public int FIFO() {
        int misses = 0;
        ArrayList<Integer> fifo = new ArrayList<Integer>();
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {

            System.out.println("Outer loop, i = " + i);
            for (int j = 0; j < pageFrames.length; j++) {
                System.out.println("Inner loop 1, j = " + j);
                if (pageFrames[j] == referenceString[i]) {
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                System.out.println("Inner loop 2, y = " + y);
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    fifo.add(y);
                    misses++;
                    continue outerloop;
                }
            }
            int firstIndex = fifo.get(0);
            fifo.remove(0);
            pageFrames[firstIndex] = referenceString[i];
            fifo.add(firstIndex);
            misses++;
        }
        return misses;
    }

    public int LRU() {
        int misses = 0;
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {

            System.out.println("Outer loop, i = " + i);
            for (int j = 0; j < pageFrames.length; j++) {
                System.out.println("Inner loop 1, j = " + j);
                if (pageFrames[j] == referenceString[i]) {
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                System.out.println("Inner loop 2, y = " + y);
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    misses++;
                    continue outerloop;
                }
            }
            int LRUIndex = 0;
            int maxSteps = 0;
            for (int k = 0; k < pageFrames.length; k++) {
                int steps = 0;
                for (int p = i - 1; p >= 0; p--) {
                    if (referenceString[p] != pageFrames[k]) {
                        steps++;
                        if (steps > maxSteps) {
                            LRUIndex = k;
                            maxSteps = steps;
                        }
                    } else {
                        if (steps > maxSteps) {
                            LRUIndex = k;
                        }
                        break;
                    }

                }
            }
            System.out.println("LRU index: " + LRUIndex);
            pageFrames[LRUIndex] = referenceString[i];
            misses++;
        }
        return misses;
    }

    public int LFU() {
        int misses = 0;
        ArrayList<Integer> fifo = new ArrayList<Integer>();
        int count[] = new int[pageFrames.length];
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {

            System.out.println("Outer loop, i = " + i);
            for (int j = 0; j < pageFrames.length; j++) {
                System.out.println("Inner loop 1, j = " + j);
                if (pageFrames[j] == referenceString[i]) {
                    count[j]++;
                    this.printPageFrames();
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                System.out.println("Inner loop 2, y = " + y);
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    count[y]++;
                    fifo.add(y);
                    misses++;
                    this.printPageFrames();
                    continue outerloop;
                }
            }
            int leastCount = count[0];
            int leastCountIndex = 0;
            for (int p = 0; p < count.length; p++) {
                if (count[p] < leastCount) {
                    leastCount = count[p];
                    leastCountIndex = p;
                }
            }
            ArrayList<Integer> leastCountsValues = new ArrayList<Integer>();
            for (int l = 0; l < count.length; l++) {
                if (count[l] == count[leastCountIndex]) {
                    leastCountsValues.add(pageFrames[l]);
                }
            }
            if (leastCountsValues.size() > 1) {
                System.out.println("More than one with least count");
                for (int k = 0; k < leastCountsValues.size(); k++) {
                    System.out.println("Value " + (k + 1) + ": " + leastCountsValues.get(k));
                }
                int index = 0;
                boolean firstIndexIsValid = false;
                int firstIndex = -1;
                while (firstIndexIsValid == false) {
                    firstIndex = fifo.get(0);
                    fifo.remove(0);
                    for (int n = 0; n < leastCountsValues.size(); n++) {
                        if (pageFrames[firstIndex] == leastCountsValues.get(n)) {
                            firstIndexIsValid = true;
                            break;
                        }
                    }
                }

                System.out.println("FIFO: " + pageFrames[firstIndex]);
//                for (int p = 0; p < leastCountsValues.size(); p++) {
//                    if (leastCountsValues.get(p) == pageFrames[firstIndex]) {
//                        index = p;
//                        break;
//                    }
//                }
//                int pageFrameIndex = 0;
//                for (int m = 0; m < pageFrames.length; m++){
//                    if (leastCountsValues.get(index) == pageFrames[m]){
//                        pageFrameIndex = m;
//                    }
//                }
                pageFrames[firstIndex] = referenceString[i];
                fifo.add(firstIndex);
                count[firstIndex] = 1;
                misses++;
                this.printPageFrames();
            } else {
                pageFrames[leastCountIndex] = referenceString[i];
                count[leastCountIndex] = 1;
                misses++;
                this.printPageFrames();
            }
        }
        return misses;
    }

    public int optimal() {
        int misses = 0;
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {

            System.out.println("Outer loop, i = " + i);
            for (int j = 0; j < pageFrames.length; j++) {
                System.out.println("Inner loop 1, j = " + j);
                if (pageFrames[j] == referenceString[i]) {
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                System.out.println("Inner loop 2, y = " + y);
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    misses++;
                    continue outerloop;
                }
            }
            int FurthestIndex = 0;
            int maxSteps = 0;
            for (int k = 0; k < pageFrames.length; k++) {
                int steps = 0;
                for (int p = i + 1; p < referenceString.length; p++) {
                    if (referenceString[p] != pageFrames[k]) {
                        steps++;
                        if (steps > maxSteps) {
                            FurthestIndex = k;
                            maxSteps = steps;
                        }
                    } else {
                        if (steps > maxSteps) {
                            FurthestIndex = k;
                        }
                        break;
                    }

                }
            }
            System.out.println("Furthest index: " + FurthestIndex);
            pageFrames[FurthestIndex] = referenceString[i];
            misses++;
        }
        return misses;
    }

    public int secondChance() {
        int misses = 0;
        int referenceBits[] = new int[pageFrames.length];
        int j = 0;
        int offset = 0;
        
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {
            for (int k = 0; k < pageFrames.length; k++){
                if (pageFrames[k] == referenceString[i]) {
                    
                    referenceBits[k] = 1;
                    System.out.println("Set Reference bit of element " + k + " = 1");
                    printPageFrames();
                     System.out.println("---------------------------------------");
                    continue outerloop;
                }
            }
            for (int q = 0; q < pageFrames.length; q++){
                if (pageFrames[q] == -1) {
                    pageFrames[q] = referenceString[i];
                    referenceBits[q] = 0;
                             System.out.println("Set Reference bit of element " + q + " = 0");
                    misses++;
                    printPageFrames();
                    System.out.println("---------------------------------------");
                    continue outerloop;
                }
            }
            while (true) {
                if (j == 3){
                    j = 0;
                }
                System.out.println(j);
                System.out.println("Reference bit of element " + j + " = " + referenceBits[j]);
                if (referenceBits[j] == 0){
                    pageFrames[j] = referenceString[i];
                    referenceBits[j] = 0;
                    System.out.println("Set Reference bit of element " + j + " = 0");
                    misses++;
                    
                    j -= offset;
                    j++;
                    printPageFrames();
                    System.out.println("---------------------------------------");
                    continue outerloop;
                }
                else{
                    referenceBits[j] = 0;
                    System.out.println("Set Reference bit of element " + j + " = 0");
                    j++;
                    offset++;
                }

//            int FurthestIndex = 0;
//            int maxSteps = 0;
//            for (int k = 0; k < pageFrames.length; k++) {
//                int steps = 0;
//                for (int p = i + 1; p < referenceString.length; p++) {
//                    if (referenceString[p] != pageFrames[k]) {
//                        steps++;
//                        if (steps > maxSteps) {
//                            FurthestIndex = k;
//                            maxSteps = steps;
//                        }
//                    } else {
//                        if (steps > maxSteps) {
//                            FurthestIndex = k;
//                        }
//                        break;
//                    }
//
//                }
//            }
//            System.out.println("Furthest index: " + FurthestIndex);
//            pageFrames[FurthestIndex] = referenceString[i];
//            misses++;
            }
        }
        return misses;
    }
}
