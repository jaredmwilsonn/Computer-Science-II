import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*=====================================================================================================
 |       Assignment: HW 03 - Implimenting a Hash Function
 |      
 |           Author: Jared Wilson
 |         Language: Java
 |
 |       To Compile: javac Hw03.java
 |
 |       To Execute: java Hw03 filename
 |
 |             Class: COP3503 - CS II Spring 2021
 |        Instructor: McAlpin
 |          Due Date: 05/02/2021
 |
 +======================================================================================================*/
 
 public class Hw03 {
    //Algorithm 1 ***based off given pseudocode***
    public static int UCFxram(String input, int len){
        int randVal1 = 0xbcde98ef;      //aribtrary value
        int randVal2 = 0x7890face;
        int hashVal = 0xfa01bc96 ;      //start seed value
        int roundedEnd = len & 0xfffffffc;      //Array d gets 4 byte blocks
        byte[] data = input.getBytes(StandardCharsets.US_ASCII);        //encodes input string into a sequence of bytes using ASCII
        int tempData = 0;

        for(int i = 0; i < roundedEnd; i += 4){
            tempData = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) | ((data[i + 2] & 0xff) <<
                        16) | (data[i + 3] << 24);
            tempData = tempData * randVal1;     //multiply
            tempData = Integer.rotateLeft(tempData, 12);        //rotate left 12 bits
            tempData = tempData * randVal2;     //another multiply
            hashVal = hashVal ^ tempData;       //XOR
            hashVal = Integer.rotateLeft(hashVal, 13);      //rotate left 13 bits
            hashVal = hashVal * 5 + 0x46b6456e;
        }
                //now collect orphan input characters
        tempData = 0;
        if((len & 0x03) == 3){
            tempData = (data[roundedEnd + 2] & 0xff) << 16;
            len = len -1;
        }

        if((len & 0x03) == 2){
            tempData |= (data[roundedEnd + 1] & 0xff) << 8;     //inclusive OR
            len = len -1;
        }

        if((len & 0x03) == 1){
            tempData |= (data[roundedEnd] & 0xff);          //inclusive OR
            tempData = tempData * randVal1;                 //multiply
            tempData = Integer.rotateLeft(tempData, 14);               //rotate left 14 bits
            tempData = tempData * randVal2;                 //another multiply
            hashVal = hashVal ^ tempData;                  //XOR
        }

        hashVal = hashVal ^ len;        //XOR
        hashVal = hashVal & 0xb6acbe58;     //AND
        hashVal = hashVal ^ hashVal >>> 13;     //zero fill right shift 13
        hashVal = hashVal * 0x53ea2b2c;     //another arbitrary value
        hashVal = hashVal ^ hashVal >>> 16;     //zero fill right shift 16
        return hashVal;     //return the 32 bit int hash
    }

    public static void complexityIndicator(){
        System.err.println("ja767326;2.2;3");       //NID; difficulty; hours spent on assignment
    }

    public static void main(String[] args){
        String input;
        int hashValue;
        if(args.length == 1){
            try{
                File file = new File(args[0]);
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()){
                    input = scanner.nextLine();     //next line of .txt file
                    hashValue = UCFxram(input, input.length());
                    System.out.format("%10x:%s\n",hashValue, input);        //2.1.1 Outputs: Hash Value
                }
                System.out.println("Input file processed");     //2.1.1 Outputs: Completion
                scanner.close();
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }
        complexityIndicator();
    }
    
}
