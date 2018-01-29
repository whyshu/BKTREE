import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Node {

    public Node(String word, double cost) {
        this.word = word;
        this.cost = cost;
    }
    //A string to hold the word
    public String word;
    //A list of nodes to hold the children of the parent node
    public List<Node> childNodes = new ArrayList<Node>();
    //Variable cost to hold the cost of the node with its root
    public double cost;
}
