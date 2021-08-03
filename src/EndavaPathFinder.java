import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class EndavaPathFinder {
    private static HashMap<Integer,String> nodesnamesint;
    // Read
    // Process path
    // Print
    // Save
    // Compress
    // user menu
    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> nodes = new ArrayList<>();
        Map<String, Integer> nodesnames = new HashMap<>();
        nodesnamesint = new HashMap<>();
        long[][] matrix = readGraph("input.txt",nodes, nodesnames);
        processPath(nodes,matrix);
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
        for (int i=0;i<nodes.size();i++){
            cost[i] = LongStream.generate(() -> Long.MAX_VALUE).limit(nl).toArray();
            paths[i] = new String[nl];
            for(int j=0;j<nl;j++){
                paths[i][j] = "";
            }
        }

        //Simpleentry
        Queue<int []> pq = new LinkedList<>();
        for(int node =0;node<nodes.size();node++){
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
        }else{
            System.out.println("El grafo no tiene solución posible");
        }


    }
}
