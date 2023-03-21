/*=============================================================================
| Assignment: HW 01 - Building and managing a BST
|
| Author: Jared Wilson
| Language: Java
|
| To Compile: javac Hw01.java
|
| To Execute: java Hw01 filename
| where filename is in the current directory and contains
| commands to insert, delete, print.
|
| Class: COP3503 - CS II Spring 2021
| Instructor: McAlpin
| Due Date: 28 Feburary 2021
|
+=============================================================================*/
import java.io.*;
class Hw01{
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
		BufferedReader reader;
        File inFile = null;
        if (0 < args.length) {
            inFile = new File(args[0]);
        } 
        else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }
        int value;
		try {
			reader = new BufferedReader(new FileReader(inFile));
			String line = reader.readLine();
            String[] part;

            System.out.println(inFile + " contains:");
            while(line != null){    // prints the .txt file
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();

            // re-opens .txt file from the begining
            reader = new BufferedReader(new FileReader(inFile));
            line = reader.readLine();
            while(line != null) {
                if(line.matches(".*\\d.*")) {
                    // (a) checks for command token i to insert new values
                    if(line.contains("i")) {  
                        part = line.split("(?<=\\D)(?=\\d)");
                        value = Integer.parseInt(part[1]);
                        bst.insert(value);
                        //System.out.println("main i1: " + bst.root.key);
                    }

                    // (b) checks for command token d to delete existing values
                    // prints to STDOUT if NOT found
                    if(line.contains("d")) { 
                        part = line.split("(?<=\\D)(?=\\d)");
                        value = Integer.parseInt(part[1]);
                        if(bst.search(value)) {
                            bst.deleteKey(value);
                        // System.out.print("deleted " + value + " \n");
                        // bst.printInorder();
                        // System.out.println();

                        }
                        else {
                            System.out.println("d " + value + ": integer " 
                                                + value + " NOT found - NOT deleted");
                        }
                    }

                    // (c) checks for command token s to search for exisiting values
                    // prints to STDOUT if NOT found
                    if(line.contains("s")) {
                        //System.out.print("search");
                        part = line.split("(?<=\\D)(?=\\d)");
                        value = Integer.parseInt(part[1]);

                        if(!bst.search(value)) {
                            System.out.println("s " + value + ": integer " 
                                                + value + " NOT found");
                        }
                        else {
                            System.out.println(value +": found");
                        }
                    }
                }

                // (d) checks for command token p to print data inorder
                else if(line.contains("p")) {
                    bst.printInorder();
                }

                // (f) checks for the command token q to exit program
                else if(line.contains("q")) {
                    bst.countChildren_Left();
                    bst.getDepth_Left();
                    bst.countChildren_Right();
                    bst.getDepth_Right();
                    bst.completixtyIndicator();
                    //System.out.println("quitting");
                    System.exit(0);
                }

                else {
                    part = line.split("(?<=\\D)(?=\\d)");
                    System.out.println( part[0] + " command: missing numeric parameter");

                }

                //System.out.println("* " + line);			
                line = reader.readLine();
            }
            
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class BinarySearchTree {
    class Node { 
        int key; 
        Node left = null, right = null; 
   
        public Node(int key){ 
            this.key = key; 
        } 
    } 
    // BST root node 
    Node root; 
  
    // constructor  // initial empty tree
    BinarySearchTree(){ 
        root = null; 
    } 

    // calls insert_Recursive
    void insert(int key)  { 
        root = insert_Recursive(root, key); 
    } 
   
    // function to insert into BST
    Node insert_Recursive(Node root, int key) { 
          //tree is empty
        if (root == null) { 
            return new Node(key); 
        } 
        //traverse the tree
        if (key < root.key) {    //insert in the left subtree
            root.left = insert_Recursive(root.left, key); 
        }
        else {   //insert in the right subtree
            root.right = insert_Recursive(root.right, key);
        }
        return root; 
    } 

    // function to find largest key in BST
    Node maxKey(Node root)
    {
        while(root.right != null) {
            root = root.right;
        }
        return root;
    }
    
    // calls delete_Recursive
    void deleteKey(int key) { 
        root = delete_Recursive(root, key); 
    } 

    // function to delete from BST
    Node delete_Recursive(Node root, int key)  { 
        // base case
        if (root == null) { 
            return root; 
        }
        if (key < root.key) {    // left subtree
            root.left = delete_Recursive(root.left, key);
        }
        else if (key > root.key) { // right subtree
            root.right = delete_Recursive(root.right, key);
        }
        else { 
            // 0 child case
            if(root.left == null && root.right == null) {
                return null;
            }
            // 2 child case
            else if (root.left != null && root.right != null) {
                Node maxVal = maxKey(root.left);
                root.key = maxVal.key;
                root.left = delete_Recursive(root.left, maxVal.key);
            }
            // 1 child case
            else {
                Node child = (root.left != null) ? root.left: root.right;
                root = child;
            }
        } 
        return root; 
    } 
   
 
    // calls printInorder_Recursive
    void printInorder() { 
        printInorder_Recursive(root);
    } 
   
    // function to print BST inorder
    void printInorder_Recursive(Node root) { 
        if(root == null) {
            return ;
        }
        printInorder_Recursive(root.left);
        System.out.print(root.key + " ");
        printInorder_Recursive(root.right);
    } 
     
    // calls search_Recursive
    //returns 1 if found 0 if not
    boolean search(int key)  {
        Node temp = root; 
        temp = search_Recursive(temp, key); 
        if (temp!= null)
            return true;
        else
            return false;
    } 
   
    // function to search for given key
    Node search_Recursive(Node root, int key)  { 
        // Base Cases: root is null or key is present at root 
        if (root == null || root.key == key) 
            return root; 
        // val is greater than root's key 
        if (root.key > key) 
            return search_Recursive(root.left, key); 
        // val is less than root's key
        return search_Recursive(root.right, key); 
    } 

    // calls countChildren_Recursive for left side of BSt
    void countChildren_Left()
    {
        System.out.println("\nleft children: \t\t" +  countChildren_Recursive(root.left));
                         
    }

    // calls countChildren_Recursive for right side of BSt
    void countChildren_Right()
    {
        System.out.println("right children: \t" +  countChildren_Recursive(root.right));
                         
    }

    //function to count number of child nodes on left and right side of BST
    int countChildren_Recursive(Node root)
    {
        if(root == null)
            return 0;
        return 1 + countChildren_Recursive(root.left) + countChildren_Recursive(root.right);
    }

    //calls getDepth_Recursive for left side of BST
    void getDepth_Left()
    {
        System.out.println("left depth: \t\t" + getDepth_Recursive(root.left));
    }

    //calls getDepth_Recursive for right side of BST
    void getDepth_Right()
    {
        System.out.println("right depth: \t\t" + getDepth_Recursive(root.right));
    }

    // function to get the depth (or height) of left and right side of BST
    int getDepth_Recursive(Node root)
    {
        if(root == null)
            return 0;
        else {
            int depthL = getDepth_Recursive(root.left);
            int depthR = getDepth_Recursive(root.right);

            if(depthL > depthR)
                return (1 + depthL);
            else
                return (1 + depthR);
        }        
    }

    // function to print NID; difficulty rating; duration in hours spent on assignment to STDERR
    void completixtyIndicator()
    {
        System.err.println("ja767326; 3.5; 11");
    }
}

/*=============================================================================
| I Jared wilson (ja767326) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/