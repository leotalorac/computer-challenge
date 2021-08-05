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
    // Save ✓
    // Compress ✓
    // user menu ✓
    public static void main(String[] args) throws IOException {
        List<Integer> nodes = new ArrayList<>();
        Map<String, Integer> nodesnames = new HashMap<>();
        nodesnamesint = new HashMap<>();
        try(Scanner sc = new Scanner(System.in)){
            printBanner();
            long[][] matrix = userFile(nodes,nodesnames,sc);
            userInteraction(matrix, nodes, nodesnames,sc);
        }
    }
    //print endava logo in console
    static void printBanner() throws FileNotFoundException{
        FileReader fr = new FileReader("banner.txt");
        try(Scanner sc = new Scanner(fr)){
            while(sc.hasNextLine()){
                System.out.println(sc.nextLine());
            }
        }
    }
    //get options from user
    static void userInteraction(long[][] matrix,List<Integer> nodes, Map<String,Integer> nodesnames,Scanner syssc){
        boolean keep =true;
        while(keep) {
            System.out.println("Which action do you want to perform?");
            System.out.println("1. Get best path passing over all rooms");
            System.out.println("2. Get best path between two rooms");
            System.out.println("3. finish");
            try{
                int option = Integer.parseInt(syssc.next());
                switch (option) {
                    case 1:
                        processPath(nodes, matrix);
                        break;
                    case 2:
                        System.out.println("Put the names of the nodes from the list of nodes: ");
                        printAllNodes(nodesnames);
                        String n1 = syssc.next();
                        String n2 = syssc.next();
                        pathTwo(nodes, matrix, nodesnames.get(n1), nodesnames.get(n2));
                        break;
                    case 3:
                        keep=false;
                        break;
                    default:
                        System.out.println("Option not allowed");
                }
            }catch (Exception e){
                System.out.println("The option must be a number and be in the allowed ones");
            }
        }
    }
    //print the list of nodes from map with names
    static void printAllNodes(Map<String,Integer> nodesnames){
        nodesnames.keySet().forEach(e ->System.out.print(e + ", "));
        System.out.println();
    }
    //charge file
    static long[][] userFile(List<Integer> nodes, Map<String,Integer> nodesnames,Scanner syssc) throws FileNotFoundException {
            System.out.println("Welcome to path finder at Endava offices ");
            File tmpDir = new File("graph.eg");
            boolean exists = tmpDir.exists();
            String filename = "graph.txt";
            if (exists) {
                System.out.println("Already a file exist, do you want use it? y/n");
                if (!syssc.next().equals("y")) {
                    System.out.println("Write the file name to continue");
                    filename = syssc.next();
                } else {
                    try {
                        decompress();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Write the file name to continue");
                filename = syssc.next();
            }
            return readGraph(filename, nodes, nodesnames);
    }
    //read from path and compress the new information
    static long[][] readGraph(String filename, List<Integer> nodes, Map<String,Integer> nodesnames ) throws FileNotFoundException {
        FileReader fr = new FileReader(filename);
        try {
            compress(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        File fd = new File(filename);
        fd.delete();
        return matrix;
    }
    //print adjacent matrix
    static void printMatrix(long[][] matrix){
        for (long[] l : matrix){
            Arrays.stream(l)
                    .forEach(e-> System.out.print(e+","));
            System.out.println();
        }
    }
    //process the all nodes path
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
        String minpath = "";
        for(int v =0;v<nodes.size();v++){
            if(answer >=cost[v][nl-1]){
                answer = cost[v][nl-1];
                minpath = paths[v][nl-1];
            }
        }
        if(! minpath.equals("")){
            System.out.println("Cost of the path: "+answer);
            System.out.println("The path is:");
            Arrays.stream(minpath.split(",")).forEach(e-> System.out.print(nodesnamesint.get(Integer.parseInt(e))+"->"));
            System.out.println();
        }else{
            System.out.println("The graph has no possible solution");
        }
    }
    //compress file
    static public void compress(String filepath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filepath);
        String outputPath = "graph.eg";
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        DeflaterOutputStream compresser = new DeflaterOutputStream(outputStream);
        int contents;
        while ((contents=inputStream.read())!=-1){
            compresser.write(contents);
        }
        compresser.close();
        inputStream.close();
        System.out.println("File compressed.......");
    }
    //decompress file
    static public void decompress() throws IOException {
        FileInputStream fis=new FileInputStream("graph.eg");
        FileOutputStream fos=new FileOutputStream("graph.txt");
        InflaterInputStream iis=new InflaterInputStream(fis);
        int data;
        while((data=iis.read())!=-1)
        {
            fos.write(data);
        }

        //close the files
        fos.close();
        iis.close();
        System.out.println("File read.......");
    }
    //get the path one to other node
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
            System.out.println("Cost of the path " + distances[u][v]);
            Arrays.stream(getpath(u,v,paths).split(",")).forEach(e-> System.out.print(nodesnamesint.get(Integer.parseInt(e))+"->"));
            System.out.println();
        }else{
            System.out.println("The path is not possible");
        }
    }
    //print the path from floyd marshall matriz
    static public String getpath(int u,int v,int[][] paths){
        if( paths[u][v] == 0){
            return "";
        }
        StringBuilder path = new StringBuilder(u + ",");
        while (u!=v){
            u=paths[u][v];
            path.append(u).append(",");
        }
        return path.toString();
    }
}
