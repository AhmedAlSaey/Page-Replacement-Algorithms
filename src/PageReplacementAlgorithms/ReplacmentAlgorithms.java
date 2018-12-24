/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PageReplacementAlgorithms;

import java.sql.Time;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ReplacmentAlgorithms {

    int referenceString[];
    int pageFrames[];
    int referenceBits[];
    int modifyBits[];
    int nextVictimPointer;

    public ReplacmentAlgorithms() {
        Random rand = new Random();
        pageFrames = new int[rand.nextInt(20) + 1];
        referenceString = new int[rand.nextInt(300 - pageFrames.length) + pageFrames.length + 1]; //Assume max size of reference string is 100 and minimum is less than the max possible size of reference string
        for (int i = 0; i < referenceString.length; i++) {
            referenceString[i] = rand.nextInt(100);
        }
        
        Arrays.fill(pageFrames, -1);
        referenceBits = new int[pageFrames.length];
        modifyBits = new int[pageFrames.length];
        nextVictimPointer = 0;
    }

    public ReplacmentAlgorithms(int referenceString[], int pageFramesSize) {
        this.referenceString = referenceString;
        this.pageFrames = new int[pageFramesSize];
        Arrays.fill(pageFrames, -1);
        referenceBits = new int[pageFrames.length];
        modifyBits = new int[pageFrames.length];
        nextVictimPointer = 0;
    }
    
//Those are the main functions, other functions are just supporting functions for testing and printing information <<<<<<<<<<<<<<<<<<<<<<<<<<
    
    public int FIFO() {
        int misses = 0;
        ArrayList<Integer> fifo = new ArrayList<Integer>();
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {
            printCurrentRererenceString(i);
            for (int j = 0; j < pageFrames.length; j++) {
                if (pageFrames[j] == referenceString[i]) {
                    printInformationFIFO(fifo);
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    fifo.add(y);
                    misses++;
                    printInformationFIFO(fifo);
                    continue outerloop;
                }
            }
            int firstIndex = fifo.get(0);
            fifo.remove(0);
            pageFrames[firstIndex] = referenceString[i];
            fifo.add(firstIndex);
            misses++;
            printInformationFIFO(fifo);
        }
        reset();
        return misses;
    }

    public int LRU() {
        int misses = 0;
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {
            printCurrentRererenceString(i);
            for (int j = 0; j < pageFrames.length; j++) {
                if (pageFrames[j] == referenceString[i]) {
                    printInformationLRU(i);
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    misses++;
                    printInformationLRU(i);
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
            System.out.println("Least recently used value is " + pageFrames[LRUIndex] + "\n");
            pageFrames[LRUIndex] = referenceString[i];
            printInformationLRU(i);
            misses++;
        }
        reset();
        return misses;
    }

    public int LFU() {
        int misses = 0;
        ArrayList<Integer> fifo = new ArrayList<Integer>();
        int count[] = new int[pageFrames.length];
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {

            for (int j = 0; j < pageFrames.length; j++) {
                if (pageFrames[j] == referenceString[i]) {
                    count[j]++;
                    printInformationLFU(referenceString[i], count, fifo);
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    count[y]++;
                    fifo.add(y);
                    misses++;
                    printInformationLFU(referenceString[i], count, fifo);
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
                int index = 0;
                boolean firstIndexIsValid = false;
                int firstIndex = 0;
                fifoLoop:
                for (int y = 0; y < fifo.size(); y++) {
                    firstIndex = fifo.get(y);
                    for (int n = 0; n < leastCountsValues.size(); n++) {
                        if (pageFrames[firstIndex] == leastCountsValues.get(n)) {
                            fifo.remove(y);
                            break fifoLoop;
                        }
                    }
                }
                pageFrames[firstIndex] = referenceString[i];
                fifo.add(firstIndex);
                count[firstIndex] = 1;
                misses++;
                printInformationLFU(referenceString[i], count, fifo);
            } else {
                pageFrames[leastCountIndex] = referenceString[i];
                count[leastCountIndex] = 1;
                for (int f = 0; f < fifo.size(); f++){
                    if (fifo.get(f) == leastCountIndex){
                        fifo.remove(f);
                        fifo.add(leastCountIndex);
                        break;
                    }
                }
                misses++;
                printInformationLFU(referenceString[i], count, fifo);
            }
        }
        reset();
        return misses;
    }

    public int optimal() {
        int misses = 0;
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {
            for (int j = 0; j < pageFrames.length; j++) {
                if (pageFrames[j] == referenceString[i]) {
                    printInformationOptimal(referenceString[i], i);
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    misses++;
                    printInformationOptimal(referenceString[i], i);
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
            pageFrames[FurthestIndex] = referenceString[i];
            misses++;
            printInformationOptimal(referenceString[i], i);
        }
        reset();
        return misses;
    }

    public int secondChance() {
        int misses = 0;
        ArrayList<Integer> secondChance = new ArrayList<Integer>();
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {

            for (int j = 0; j < pageFrames.length; j++) {
                if (pageFrames[j] == referenceString[i]) {
                    referenceBits[j] = 1;
                    printInformationSCA(referenceString[i], secondChance);
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    secondChance.add(y);
                    referenceBits[y] = 0;
                    misses++;
                    printInformationSCA(referenceString[i], secondChance);
                    continue outerloop;
                }
            }
            int loopIndex = 0;
            while (true) {
                if (loopIndex == secondChance.size()) {
                    loopIndex = 0;
                }
                int firstIndex = secondChance.get(loopIndex);
                if (referenceBits[firstIndex] == 1) {
                    referenceBits[firstIndex] = 0;
                    loopIndex++;
                } else if (referenceBits[firstIndex] == 0) {
                    referenceBits[firstIndex] = 0;
                    pageFrames[firstIndex] = referenceString[i];
                    secondChance.remove(loopIndex);
                    secondChance.add(firstIndex);
                    misses++;
                    printInformationSCA(referenceString[i], secondChance);
                    break;
                }
            }
        }
        reset();
        return misses;
    }

    public int enhancedSecondChance() {
        int misses = 0;
        outerloop:
        for (int i = 0; i < referenceString.length; i++) {
            for (int j = 0; j < pageFrames.length; j++) {
                if (pageFrames[j] == referenceString[i]) {
                    printInformationESCA(referenceString[i]);
                    continue outerloop;
                }
            }
            for (int y = 0; y < pageFrames.length; y++) {
                if (pageFrames[y] == -1) {
                    pageFrames[y] = referenceString[i];
                    referenceBits[y] = 1;
                    Random rand = new Random();
                    modifyBits[y] = rand.nextInt(2);
                    misses++;
                    nextVictimPointer = nextVictimPointer == pageFrames.length - 1 ? 0: nextVictimPointer + 1;
                    printInformationESCA(referenceString[i]);
                    continue outerloop;
                }
            }
            int nextVictimBefore = nextVictimPointer;
            int zeroZeroLoopPointer = nextVictimPointer;
            int zeroZeroStepCount = 0;
            int  zeroOneLoopPointer = nextVictimPointer;
            int zeroOneStepCount = 0;
            boolean done = false;
            while (done == false) {
                while (done == false) {
                    if (zeroZeroStepCount == pageFrames.length) {
                        zeroZeroLoopPointer = nextVictimPointer;
                        zeroZeroStepCount = 0;
                        break;
                    }
                    if (zeroZeroLoopPointer == pageFrames.length) {
                        zeroZeroLoopPointer = 0;
                    }
                    if (referenceBits[zeroZeroLoopPointer] == 0 && modifyBits[zeroZeroLoopPointer] == 0) {
                        pageFrames[zeroZeroLoopPointer] = referenceString[i];
                        referenceBits[zeroZeroLoopPointer] = 1;
                        Random rand = new Random();
                        modifyBits[zeroZeroLoopPointer] = rand.nextInt(2);
                        nextVictimPointer = (zeroZeroLoopPointer == pageFrames.length - 1 ? 0 : zeroZeroLoopPointer + 1);
                        misses++;
                        printInformationESCA(referenceString[i]);
                        done = true;
                    } else {
                        zeroZeroLoopPointer++;
                        zeroZeroStepCount++;
                    }
                }
                while (done == false) {
                    if (zeroOneStepCount == pageFrames.length) {
                        zeroOneLoopPointer = nextVictimPointer;
                        zeroOneStepCount = 0;
                        break;
                    }
                    if (zeroOneLoopPointer == pageFrames.length) {
                        zeroOneLoopPointer = 0;
                    }
                    if (referenceBits[zeroOneLoopPointer] == 0 && modifyBits[zeroOneLoopPointer] == 1) {
                        pageFrames[zeroOneLoopPointer] = referenceString[i];
                        referenceBits[zeroOneLoopPointer] = 1;
                        Random rand = new Random();
                        modifyBits[zeroOneLoopPointer] = rand.nextInt(2);
                        nextVictimPointer = (zeroOneLoopPointer == pageFrames.length - 1 ? 0 : zeroOneLoopPointer + 1);
                        misses++;
                        printInformationESCA(referenceString[i]);
                        done = true;
                    } else if (referenceBits[zeroOneLoopPointer] == 1) {
                        referenceBits[zeroOneLoopPointer] = 0;
                        zeroOneLoopPointer++;
                        zeroOneStepCount++;
                    } else {
                        zeroOneLoopPointer++;
                        zeroOneStepCount++;
                    }
                }
            }

        }
        reset();
        return misses;
    }
    
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    public void printInformationESCA(int value){
        System.out.println("Value to insert: " + value + "\n");
        for (int i = 0; i < pageFrames.length; i++){
            
            String output = "Element number " + i + " in page frames = " + pageFrames[i] + ", it's r bit is " + referenceBits[i] + " and it's m bit is " + modifyBits[i];
            
            System.out.println(output);
        }
        System.out.println("\nNext victim is currently the frame in pointer " + nextVictimPointer);
        System.out.println("----------------------------------------------");
        
    }
    
    public void printInformationFIFO(ArrayList<Integer> fifo){
        for (int i = 0; i < pageFrames.length; i++){
            String output = "Element number " + i + " in page frames = " + pageFrames[i];
            
            System.out.println(output);
        }
        System.out.println();
        String FIFOOrder = "Current FIFO order (left value is FIFO): [";
        for (int j = 0; j < fifo.size(); j++){
            FIFOOrder += pageFrames[fifo.get(j)] + ",";
        }
        FIFOOrder = FIFOOrder.substring(0, FIFOOrder.length() - 1);
        FIFOOrder += "]";
        System.out.println(FIFOOrder);
        System.out.println("----------------------------------------------");
    }
    
    public void printInformationLRU(int index){
        for (int i = 0; i < pageFrames.length; i++){
            String output = "Element number " + i + " in page frames = " + pageFrames[i];
            
            System.out.println(output);
        }
        System.out.println();
        String useOrder = "Previous use order (left value is used first): [";
        for (int j = 0; j <= index; j++){
            useOrder += referenceString[j] + ",";
        }
        if (!useOrder.endsWith("[")){
        useOrder = useOrder.substring(0, useOrder.length() - 1);
        }
        useOrder += "]";
        System.out.println(useOrder);
        System.out.println("----------------------------------------------");
    }
    
    public void printInformationLFU(int value, int count[], ArrayList<Integer> fifo){
        System.out.println("Value to insert: " + value + "\n");
        for (int i = 0; i < pageFrames.length; i++){
            String output = "Element number " + i + " in page frames = " + pageFrames[i];
            
            System.out.println(output);
        }
        System.out.println();
        String countString = "Usage frequency of elements in page frame (leftmost is first element in page frame): [";
        for (int j = 0; j < count.length; j++){
            countString += count[j] + ",";
        }
        if (!countString.endsWith("[")){
        countString = countString.substring(0, countString.length() - 1);
        }
        countString += "]";
        System.out.println(countString + "\n");
        String fifoString = "FIFO  order (leftmost is  first in/out): [";
        for (int j = 0; j < fifo.size(); j++){
            fifoString += pageFrames[fifo.get(j)] + ",";
        }
        if (!fifoString.endsWith("[")){
        fifoString = fifoString.substring(0, fifoString.length() - 1);
        }
        fifoString += "]";
        System.out.println(fifoString + "\n");
        System.out.println("----------------------------------------------");
    }
    
    public void printInformationOptimal(int value, int index){
        System.out.println("Value to insert: " + value + "\n");
        for (int i = 0; i < pageFrames.length; i++){
            String output = "Element number " + i + " in page frames = " + pageFrames[i];
            
            System.out.println(output);
        }
        System.out.println();
        String useOrder = "Next reference order (left value will be referenced first): [";
        for (int j = index + 1; j < referenceString.length; j++){
            useOrder += referenceString[j] + ",";
        }
        if (!useOrder.endsWith("[")){
        useOrder = useOrder.substring(0, useOrder.length() - 1);
        }
        useOrder += "]";
        System.out.println(useOrder);
        System.out.println("----------------------------------------------");
    }
    
    public void printInformationSCA(int value, ArrayList<Integer> fifo){
        System.out.println("Value to insert: " + value + "\n");
        for (int i = 0; i < pageFrames.length; i++){
            
            String output = "Element number " + i + " in page frames = " + pageFrames[i] + ", it's r bit is " + referenceBits[i];
            
            System.out.println(output);
        }
        System.out.println();
        String fifoString = "FIFO  order (leftmost is  first in/out): [";
        for (int j = 0; j < fifo.size(); j++){
            fifoString += pageFrames[fifo.get(j)] + ",";
        }
        if (!fifoString.endsWith("[")){
        fifoString = fifoString.substring(0, fifoString.length() - 1);
        }
        fifoString += "]";
        System.out.println(fifoString + "\n");
        System.out.println("----------------------------------------------");
        
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
    
    public void reset(){
        Arrays.fill(pageFrames, -1);
        referenceBits = new int[pageFrames.length];
        modifyBits = new int[pageFrames.length];
        nextVictimPointer = 0;
    }

    public void generateRandomReferenceString() {
        Random rand = new Random();
        for (int i = 0; i < referenceString.length; i++) {
            referenceString[i] = rand.nextInt(100);
        }
    }
    
    public void printCurrentRererenceString(int i){
        System.out.println("Current reference string value = " + referenceString[i] + "\n");
    }
}
