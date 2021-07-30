import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;


public class EndavaPathFinder {
    private static boolean visited[];
    static ArrayList<ArrayList<Integer>> adj;
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fr = new FileReader("input.txt");
       adj = new ArrayList<>();
        Scanner sc = new Scanner(fr);
        String str;
        List<Integer> nodes = new ArrayList<>();
        HashMap<String,Integer> nodesnames = new HashMap<>();
        String line="";
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            nodes.add(-1);
            nodesnames.put(line, nodes.size() - 1);
            if (line.contains(",")) {
                break;
            }
        }
        int[][] matrix = new int[nodes.size()-1][nodes.size()-1];
        for (int i=0;i<10;i++)
            adj.add(new ArrayList<>());
        String[] edge;
        do{
            edge = line.split(",");
            matrix[nodesnames.get(edge[0])][nodesnames.get(edge[1])] = Integer.parseInt(edge[2]);
            matrix[nodesnames.get(edge[1])][nodesnames.get(edge[0])] = Integer.parseInt(edge[2]);
            adj.get(nodesnames.get(edge[0])).add(nodesnames.get(edge[1]));
            adj.get(nodesnames.get(edge[1])).add(nodesnames.get(edge[0]));
            line = sc.nextLine();
        }while (sc.hasNextLine());
        for(ArrayList m : adj){
            Arrays.stream(m.toArray())
                    .forEach(e -> System.out.print(e + ","));
            System.out.println();
        }
        visited = new boolean[nodes.size()];
        DFS(0);
    }
    static void DFS(int vertex) {
        visited[vertex] = true;
        System.out.print(vertex + " ");

        Iterator<Integer> ite = adj.get(vertex).listIterator();
        while (ite.hasNext()) {
            int adj = ite.next();
            if (!visited[adj])
                DFS(adj);
        }
}
}
