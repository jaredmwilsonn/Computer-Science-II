/* COP3503 - CS II
 * Lab 01 - Prim's Algorithm
 * Submitted by:
 *      Jared Wilson
 */

import java.io.*;

public class Lab01{
        //finds vertex with minimum key value not yet in MST
       static int minKey(double key[], boolean mstSet[], int maxVertices){
            double min = Integer.MAX_VALUE;     //initialize min value
            int minIndex = -1;
            for(int v = 0; v < maxVertices; v++){
                if(mstSet[v] == false && key[v] < min){
                    min = key[v];
                    minIndex = v;
                }
            }
            return minIndex;
        }

        //constructs MST
       static void MST_PRIM(double graph[][], int maxVertices){
            int parent[] = new int[maxVertices];                //store constructed MST
            double key[] = new double[maxVertices];             //used to pick minimum weight edge
            boolean mstSet[] = new boolean[maxVertices];        //represents set of vertices included in MST
            int i;

            for(i = 0; i < maxVertices; i++){   //initialize all keys as infinite
                key[i] = Integer.MAX_VALUE; 
                mstSet[i] = false;
            }

            key[0] = 0;         //first vertex
            parent[0] = -1;     //first node is always root of MST

            for(i = 0; i < maxVertices - 1; i++){
                int u = minKey(key, mstSet, maxVertices);       //gets the minimum key vertex not yet in MST
                mstSet[u] = true;                               //add the vertex to MST Set
                    
                for(int v = 0; v < maxVertices; v++){           //update key and parent of adjacent vertices not yet in MST
                    if(graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < key[v]){
                        parent[v] = u;
                        key[v] = graph[u][v];
                    }
                }
            }
            printMST(parent, graph, maxVertices);   //print MST
        }

        //print
        static void printMST(int parent[], double graph[][], int maxVertices){
            double result = 0;
            for(int i = 1; i < maxVertices; i++){
                System.out.printf(parent[i] + "-" + i + "\t" + "%.5f\n", graph[parent[i]][i]);  //prints weight with 5 significant digits
                result += graph[parent[i]][i];  //sum of weight
            }
            System.out.printf("%.5f\n", result);
        }

        public static void main(String args[]){

            try{
                File file = new File(args[0]);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                int maxVertices = Integer.parseInt(reader.readLine().trim());
                double graph[][] = new double [maxVertices][maxVertices];
                int edges = Integer.parseInt(reader.readLine().trim());

                for(int i = 0; i < edges; i++){
                    String line[] = reader.readLine().split(" ");
                    int vert = Integer.parseInt(line[0]);
                    int otherVert = Integer.parseInt(line[1]);
                    double weight = Double.parseDouble(line[2]);
                    graph[vert][otherVert] = weight;
                    graph[otherVert][vert] = weight;
                }
                MST_PRIM(graph, maxVertices);
                reader.close();

            }
            catch(IOException e){
                e.printStackTrace();
            }

           
        }  
}
