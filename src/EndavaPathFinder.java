import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.*;

public class EndavaPathFinder {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fr = new FileReader("input.txt");
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
        String[] edge;
        do{
            edge = line.split(",");
            matrix[nodesnames.get(edge[0])][nodesnames.get(edge[1])] = Integer.parseInt(edge[2]);
            matrix[nodesnames.get(edge[1])][nodesnames.get(edge[0])] = Integer.parseInt(edge[2]);
            line = sc.nextLine();
        }while (sc.hasNextLine());
        for(int[]m : matrix){
            Arrays.stream(m).forEach(e -> System.out.print(e + ","));
            System.out.println();
        }
    }
}
