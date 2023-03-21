/*=====================================================================================================
 |       Assignment: HW 02 - Implimenting a Skip List
 |      
 |           Author: Jared Wilson
 |         Language: Java
 |
 |       To Compile: javac Hw01.java
 |
 |       To Execute: java Hw01 filename
 |                        where filename is in the current directory and contains
 |                              commands to insert, delete, and print.
 |
 |             Class: COP3503 - CS II Spring 2021
 |        Instructor: McAlpin
 |          Due Date: 04/04/2021
 |
 +======================================================================================================*/
 import java.io.*;
 import java.util.*;
 
 public class Hw02{
    public static String[] commandList;
    
    public Hw02(String file){
        commandList = new String[500];
        int i = 0;
        Scanner scanner = null;
        try{
            scanner = new Scanner(new File(file));
        }
        catch(IOException e){
            e.printStackTrace();
        }

        while(scanner.hasNext()){
            commandList[i] = scanner.next();
            i++;
        }

        i = 0;
        System.out.println("For the input file named " + file);
    }

    //completity indicator
    public static void complexityIndicator(){
        System.err.println("ja767326;3.3;6");   //NID; difficulty; hours spent on assignment
    }

    //main
    public static void main(String[] args){
        int i = 0, level = 1, temp;
        String fileName = args[0];
        String seedMessage;
        Integer seed;

        // arguments > 1 then random seed
        if(args.length > 1){
            seed = Integer.parseInt(args[1]);
            seedMessage = "With the RNG seeded with " + seed + ",";
        }

        //otherwise (<=1) set seed = 42 (unseeded)
        else{
            seed = 42;
            seedMessage = "With the RNG unseeded,";
        }

        //new object for file
        new Hw02(fileName);
        System.out.println(seedMessage);    //prints seedMessage
        SkipList list = new SkipList(seed);
        BuildSkipList[] skip = list.buildSkipList();

        for(i = 0; i < commandList.length; i++){
            if(commandList[i] != null){
                if(commandList[i].equals("i")){     //insert command calls insert
                    i++;
                    temp = list.insert(Integer.parseInt(commandList[i]), skip);
                    if(temp > level){
                        level = temp;
                    }
                }
                else if(commandList[i].equals("d")){    //delete command calls delete
                    i++;
                    list.delete(Integer.parseInt(commandList[i]), skip, level);
                }
                else if(commandList[i].equals("s")){    //search command calls search
                    i++;
                    list.search(Integer.parseInt(commandList[i]), skip, level);
                }
                else if(commandList[i].equals("p")){    //print command calls printAll
                    list.printAll(skip, level, commandList.length);
                }
                else if(commandList[i].equals("q")){    //quit command calls quit
                    list.quit();
                }
                else{      //no command or command not found
                    System.out.println("FAILED TO READ COMMAND");
                }
               }
           }
       }
   }

 class Node{
     public Node(int data){
         this.data = data;
     }
     public int data;
     public Node next;
 }

class BuildSkipList{
    public Node head = null;
    public Node tail = null;

    //called from insert
    //builds/ creates skip listnode in ascending order
    public void build(int data){
        Node temp = new Node(data); //allocate memory for new nodes

        if(head == null){
            head = tail = temp;
        }
        else if(data < head.data){
            temp.next = this.head;
            this.head = temp;
        }
        else if(data > tail.data){
            this.tail.next = temp;
            this.tail = temp;
        }
        else{   //set current node to head node
            Node current = head;
            while(current.next.data <= data){
                current = current.next;
            }
            temp.next = current.next;
            current.next = temp;
        }
    }

    //called from delete
    //deletes node containing data
    public Boolean deleteNode(int data){
        if(head == null){
            return false;
        }
        else if(head == tail && head.data == data){ //if head is tail
            head = tail = null;
            return true;
        }
        else if(head.data == data){
            head = head.next;
            return true;
        }
        else{   //progress current to next
            for(Node current = head; current.next != null; current = current.next){
                if(current.next.data == data){
                    current.next = current.next.next;
                    return true;
                }
            }

        }
        return false;
    }

    //returns true if found, false if null or not found
    public Boolean searchNode(int data){
        if(head == null){
            return false;
        }
        else if(head == tail && head.data == data){
            return true;
        }
        else if(head.data == data){
            return true;
        }
        else{
            for(Node current = head; current.next != null; current = current.next){
                if(current.next.data == data){
                    return true;
                }
            }
        }
        return false;
    }
 }

 class SkipList{
     public static Random rand = new Random();
     public static int seed;
     public SkipList(Integer seed){
         rand.setSeed(seed);
     }

     public BuildSkipList[] buildSkipList(){    //return level
         BuildSkipList[] level = new BuildSkipList[15];
         for(int i = 0; i < 15; i++){
             level[i] = new BuildSkipList();
             level[i].build(Integer.MIN_VALUE);
             level[i].build(Integer.MAX_VALUE);
         }
         return level;
     }

     //2.2.1 Insert/promote method
     //uses the single character 'i' as command token
     //inserts the integer given from .txt file into skip list in ascending order
     public int insert(int item, BuildSkipList[] level){
         int i = 0;
         for(Node current = level[i].head; current.next != null; current = current.next){
             if(current.data == item){
                 return i + 1;
             }
         }
         level[i].build(item);
         while(randFunction()){
             i++;
             level[i].build(item);
         }
         return i + 1;
     }
     //2.2.2 Delete Method
     //uses the single character 'd' as the command token
     //deletes the integer given from .txt file from the skip list
     public void delete(int item, BuildSkipList[] level, int top){
         boolean delete = false;
         for(int i = top - 1; i >= 0; i--){
             if(level[i].deleteNode(item)){
                 delete = true;
             }
         }
         if(delete){ //integer found
             System.out.println(item + " deleted");
         }
         else{  //integer not found
            System.out.println(item + " integer not found - delete not successful");
         }
         return;
     }

     //2.2.3 search method
     //uses the single character 's' as the command token
     //searches for the integer given from .txt file in the skip list
     public void search(int item, BuildSkipList[] level, int top){
         boolean find = false;
         for(int i = top - 1; i >= 0 && !find; i--){
             if(level[i].searchNode(item)){ //if item is found
                 find = true; 
             }
         }
         if(find){ //search successful
             System.out.println(item + " found");
         }
         else{  //search NOT successful
             System.out.println(item+ " NOT FOUND");
         }
         return;
     }

     //2.2.4
     //uses the single character 'p' as the command token
     //prints the contents of the whole skip list in the specified format
     public void printAll(BuildSkipList[] level, int topLevel, int maxLength)
     {
         System.out.println("the current Skip List is shown below:");
         int count;
         String[] list = new String[maxLength]; //temp list to be printed
         for(int i = 0; i < topLevel; i++){
             count = 0; //reset count
             String checkString;
             if(i == 0){    //1st loop
                 for(Node current = level[i].head; current != null; current = current.next){    //head of each level; while not null; next node
                     list[count] = " " + current.data + "; ";   //appends ; to list to match sample outputs
                     count++;
                     maxLength = count;
                 }
             }
             else{      //all subsequent loops
                 for(Node current = level[i].head; current != null; current = current.next){    //head of each level; while not null; next node
                     count = 0;
                     checkString = " " + current.data + "; ";   //appends ; to list to match sample outputs
                     if(current.data != Integer.MAX_VALUE && current.data != Integer.MIN_VALUE){    //to not alter -oo & +oo
                         for(int j = 1; j < i; j++){
                             checkString += " " + current.data + "; ";
                         }
                         while(!checkString.equals(list[count])){   /*check for duplicates */
                             count++;
                         }
                         list[count] += " " + current.data + "; ";
                     }
                 }
             }
         }
         for(int k = 0; k < maxLength; k++){
            if(list[k].equals(" " + Integer.MIN_VALUE + "; ")){ //find smallest value in list
                System.out.println("---infinity");
            }
            else if(list[k].equals(" " + Integer.MAX_VALUE+ "; ")){ //find largest value in list
                System.out.println("+++infinity");
            }
            else{
                System.out.println(list[k]);    //print the list values
            }
        }
        System.out.println("---End of Skip List---");
     }

     //2.2.5
     //uses the single character 'q' as the command token
     //causes the program to close all files and terminate
     public void quit(){
         System.exit(0);
     }

     //generates a "random number" then modulo 2 to generate a 1 or a 0
     public Boolean randFunction(){
         return (1 == Math.abs((rand.nextInt() % 2)));
     }
    }

    
 

        


/*=============================================================================
 |   I Jared Wilson (3848496) affirm that this program is 
 | entirely my own work and that I have neither developed my code together with
 | any other person, nor copied any code from any other person, nor permitted
 | my code to be copied or otherwise used by any other person, nor have I
 | copied, modified, or otherwise used programs created by others. I acknowledge
 | that any violated of the above terms will be treated as academic dishonesty.
 +=============================================================================*/