import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;


public class EndavaPathFinder {
    private static boolean[] visited;
    static ArrayList<ArrayList<Integer>> adj;
    // Read
    // Process path
    // Print
    // Save
    // Compress
    // user menu
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Integer> nodes = new ArrayList<>();
        HashMap<String,Integer> nodesnames = new HashMap<>();
        int[][] matrix =ReadGraph("input.txt",nodes,nodesnames);
        PrintMatrix(matrix);
        ProcessPath(nodes,matrix);
    }
    static int[][] ReadGraph(String filename, ArrayList<Integer> nodes,HashMap<String,Integer> nodesnames) throws FileNotFoundException {
        FileReader fr = new FileReader(filename);
        String line="";
        int[][] matrix;
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
            }
            matrix = new int[nodes.size()][nodes.size()];
            String[] edge;
            edge = line.split(",");
            matrix[nodesnames.get(edge[0])][nodesnames.get(edge[1])] = Integer.parseInt(edge[2]);
            matrix[nodesnames.get(edge[1])][nodesnames.get(edge[0])] = Integer.parseInt(edge[2]);
            do {
                line = sc.nextLine();
                edge = line.split(",");
                matrix[nodesnames.get(edge[0])][nodesnames.get(edge[1])] = Integer.parseInt(edge[2]);
                matrix[nodesnames.get(edge[1])][nodesnames.get(edge[0])] = Integer.parseInt(edge[2]);
            }while (sc.hasNextLine());
        }
        return matrix;
    }
    static void PrintMatrix(int[][] matrix){
        for (int[] l : matrix){
            Arrays.stream(l)
                    .forEach(e->{
                        System.out.print(e+",");
                    });
            System.out.println();
        }
    }
    static void ProcessPath(ArrayList<Integer> nodes, int[][] matrix){
        int nl =(int) Math.pow(2, nodes.size());
        int n = nodes.size();
        int[][] cost = new int[nodes.size()][nl];
        for (int i=0;i<nodes.size();i++){
            cost[i] = IntStream.generate(() -> (int)Math.pow(2,30)).limit(nl).toArray();
        }
        Queue<int []> pq = new LinkedList<>();
        for(int node =0;node<nodes.size();node++){
            pq.add(new int[] {node,(int)Math.pow(2,node)});
            cost[node][(int) Math.pow(2,node)] = 0;
        }
        while (! pq.isEmpty()){
            int current = pq.peek()[0];
            int mask = pq.peek()[1];
            pq.remove();
            for(int child = 0; child < nodes.size(); child++){
                if(matrix[current][child] != 0){
                    int add = matrix[current][child];
                    if(cost[child][mask | (int)Math.pow(2,child)] > cost[current][mask]+add){
                        pq.add(new int[] {child,mask | (int)Math.pow(2,child)});
                        cost[child][mask | (int)Math.pow(2,child)] = cost[current][mask]+add;
                    }
                }
            }
        }
        int answer = (int) Math.pow(2,20);
        for(int v =0;v<nodes.size();v++){
            answer = Math.min(answer,cost[v][(int)Math.pow(2,n)-1]);
        }
        PrintMatrix(cost);
        System.out.println("ans "+answer);
    }
}
