import java.io.*;
import java.util.*;
import java.util.stream.LongStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


public class EndavaPathFinder {
    private static HashMap<Integer,String> nodesnamesint;
    // Read ✓
    // Process path ✓
    // Print ✓
    // Save
    // Compress
    // user menu
    public static void main(String[] args) throws IOException {
        List<Integer> nodes = new ArrayList<>();
        Map<String, Integer> nodesnames = new HashMap<>();
        nodesnamesint = new HashMap<>();
        long[][] matrix = readGraph("input.txt",nodes, nodesnames);

        compress("input.txt");
        decompress("out.txt");
        processPath(nodes,matrix);
        pathTwo(nodes,matrix,nodesnames.get("n1"),nodesnames.get("n2"));
        //pathTwo(nodes,matrix,8,0);
    }
    static long[][] readGraph(String filename, List<Integer> nodes, Map<String,Integer> nodesnames ) throws FileNotFoundException {
        FileReader fr = new FileReader(filename);
        String line="";
        long[][] matrix;
        try(Scanner sc = new Scanner(fr)){
            int i = 0;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.contains(",")) {
                    break;
                }
                nodes.add(i);
                i++;
                nodesnames.put(line, nodes.size() - 1);
                nodesnamesint.put(nodes.size()-1,line);
            }
            matrix = new long[nodes.size()][nodes.size()];
            String[] edge;
            edge = line.split(",");
            matrix[nodesnames.get(edge[0])][nodesnames.get(edge[1])] = Long.parseLong(edge[2]);
            matrix[nodesnames.get(edge[1])][nodesnames.get(edge[0])] = Long.parseLong(edge[2]);
            do {
                line = sc.nextLine();
                edge = line.split(",");
                matrix[nodesnames.get(edge[0])][nodesnames.get(edge[1])] = Long.parseLong(edge[2]);
                matrix[nodesnames.get(edge[1])][nodesnames.get(edge[0])] = Long.parseLong(edge[2]);
            }while (sc.hasNextLine());
        }
        return matrix;
    }
    static void printMatrix(long[][] matrix){
        for (long[] l : matrix){
            Arrays.stream(l)
                    .forEach(e-> System.out.print(e+","));
            System.out.println();
        }
    }
    static void processPath(List<Integer> nodes, long[][] matrix){
        int nl =(int) Math.pow(2, nodes.size());
        int n = nodes.size();
        long [][] cost = new long[n][nl];
        String[][] paths = new String[n][nl];
        //Simpleentry
        Queue<int []> pq = new LinkedList<>();
        for(int node =0;node<nodes.size();node++){
            cost[node] = LongStream.generate(() -> Long.MAX_VALUE).limit(nl).toArray();
            paths[node] = new String[nl];
            for(int j=0;j<nl;j++){
                paths[node][j] = "";
            }
            pq.add(new int[] {node,(int)Math.pow(2,node)});
            cost[node][(int) Math.pow(2,node)] = 0;
            paths[node][(int) Math.pow(2,node)] = String.valueOf(node);
        }
        while (! pq.isEmpty()){
            int current = pq.peek()[0];
            int mask = pq.peek()[1];
            pq.remove();
            for(int child = 0; child < nodes.size(); child++){
                if(matrix[current][child] != 0){
                    long add = matrix[current][child];
                    String path = paths[current][mask];
                    if(cost[child][mask | (int)Math.pow(2,child)] > cost[current][mask]+add && !path.contains(String.valueOf(child))){
                        pq.add(new int[] {child,mask | (int)Math.pow(2,child)});
                        cost[child][mask | (int)Math.pow(2,child)] = cost[current][mask]+add;
                        paths[child][mask | (int)Math.pow(2,child)] = paths[current][mask] +","+ child;
                    }
                }
            }
        }
        long answer = Long.MAX_VALUE;
        String pans = "";
        for(int v =0;v<nodes.size();v++){
            if(answer >=cost[v][nl-1]){
                answer = cost[v][nl-1];
                pans = paths[v][nl-1];
            }
        }
        System.out.println("Costo del camino "+answer);
        if(! pans.equals("")){
            System.out.println("El camino es:");
            Arrays.stream(pans.split(",")).forEach(e-> System.out.print(nodesnamesint.get(Integer.parseInt(e))+","));
            System.out.println();
        }else{
            System.out.println("El grafo no tiene solución posible");
        }
    }
    static public void compress(String filepath) throws IOException {
        //Instantiating the FileInputStream
        FileInputStream inputStream = new FileInputStream(filepath);
        //Instantiating the FileOutputStream
        String outputPath = "out.txt";
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        //Instantiating the DeflaterOutputStream
        DeflaterOutputStream compresser = new DeflaterOutputStream(outputStream);
        int contents;
        while ((contents=inputStream.read())!=-1){
            compresser.write(contents);
        }
        compresser.close();
        System.out.println("File compressed.......");
    }
    static public void decompress(String filepath) throws IOException {
        //assign Input File : file2 to FileInputStream for reading data
        FileInputStream fis=new FileInputStream(filepath);

        //assign output file: file3 to FileOutputStream for reading the data
        FileOutputStream fos=new FileOutputStream("file3");

        //assign inflaterInputStream to FileInputStream for uncompressing the data
        InflaterInputStream iis=new InflaterInputStream(fis);

        //read data from inflaterInputStream and write it into FileOutputStream
        int data;
        while((data=iis.read())!=-1)
        {
            fos.write(data);
        }

        //close the files
        fos.close();
        iis.close();
    }
    static public void pathTwo(List<Integer> nodes, long[][] matrix,int u,int v){
        //Use floyd marshall algorithm
        int n = nodes.size();
        long[][] distances = new long[nodes.size()][nodes.size()];
        int[][] paths = new int[n][n];
        for(int i=0;i<nodes.size();i++){
            distances[i] = LongStream.generate(()->Integer.MAX_VALUE).limit(nodes.size()).toArray();
            for(int j=0;j<nodes.size();j++){
                if(i == j){
                    distances[i][j]=0;
                    paths[i][j] =j;
                } else if(matrix[i][j] !=0){
                    distances[i][j] =matrix[i][j];
                    paths[i][j] = j;
                }
            }
        }
        for(int k =0;k<nodes.size();k++){
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    if(distances[i][j] > distances[i][k] + distances[k][j]){
                        distances[i][j]= distances[i][k] + distances[k][j];
                        paths[i][j] = paths[i][k];
                    }
                }
            }
        }
        if(!getpath(u,v,paths).equals("")){
            System.out.println("Distancia de " + distances[u][v]);
            Arrays.stream(getpath(u,v,paths).split(",")).forEach(e-> System.out.print(nodesnamesint.get(Integer.parseInt(e))+","));
            System.out.println();
        }else{
            System.out.println("No existe un camino");
        }
    }
    static public String getpath(int u,int v,int[][] paths){
        if( paths[u][v] == 0){
            return "";
        }
        String path = u +",";
        while (u!=v){
            u=paths[u][v];
            path += u + ",";
        }
        return path;
    }
}
